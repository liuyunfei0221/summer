package com.blue.media.component.file.processor.impl;

import com.blue.basic.common.base.Monitor;
import com.blue.basic.constant.media.ByteHandlerType;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.processor.inter.ByteHandler;
import com.blue.media.config.deploy.LocalDiskFileDeploy;
import org.slf4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertAttachmentType;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.PAYLOAD_TOO_LARGE;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Symbol.PERIOD;
import static com.blue.basic.constant.media.ByteHandlerType.LOCAL_DISK;
import static com.blue.media.common.MediaCommonFunctions.*;
import static java.lang.System.currentTimeMillis;
import static java.nio.channels.FileChannel.open;
import static java.nio.file.StandardOpenOption.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.io.buffer.DataBufferUtils.readByteChannel;
import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static reactor.core.publisher.BufferOverflowStrategy.ERROR;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.using;


/**
 * local disk byte operate handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused", "ResultOfMethodCallIgnored"})
public final class LocalDiskByteHandler implements ByteHandler {

    private static final Logger LOGGER = getLogger(LocalDiskByteHandler.class);

    public LocalDiskByteHandler(LocalDiskFileDeploy localDiskFileDeploy) {
        validTypes = new HashSet<>(localDiskFileDeploy.getValidTypes());
        invalidPres = new HashSet<>(localDiskFileDeploy.getInvalidPres());
        nameLenThreshold = localDiskFileDeploy.getNameLenThreshold();
        descPath = localDiskFileDeploy.getDescPath();
        singleFileSizeThreshold = localDiskFileDeploy.getSingleFileSizeThreshold();
    }

    private static final int RANDOM_FILE_NAME_POST_LEN = 8;

    private static final int BACKPRESSURE_BUFFER_MAX_SIZE = 128;

    private static final String SUCCESS_MSG = "upload success";

    private Set<String> validTypes;

    private Set<Character> invalidPres;

    private long singleFileSizeThreshold;

    private int nameLenThreshold;

    private final String descPath;

    /**
     * assert media
     */
    private final Function<Part, String> PART_NAME_GETTER = part -> {
        if (isNull(part))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "part can't be empty");

        FilePart filePart = (FilePart) part;
        String fileName = filePart.filename();
        if (isBlank(fileName))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "media name can't be blank");

        int nameLength = fileName.length();
        if (nameLength > nameLenThreshold)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "media (" + fileName + ") name length can't be greater than " + nameLenThreshold);

        int index = lastIndexOf(fileName, PERIOD.identity);
        if (index == -1 || nameLength - 1 == index)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "media (" + fileName + ") has a unknown type");

        String postName = fileName.substring(index).toUpperCase();
        if (!validTypes.contains(postName))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "media (" + fileName + ") type is invalid");

        String preName = fileName.substring(0, index);
        int preLength = preName.length();
        for (int i = 0; i < preLength; i++) {
            char c = preName.charAt(i);
            if (invalidPres.contains(c))
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "media (" + fileName + ") name's prefix contains invalid str (" + c + ")");
        }

        return fileName;
    };

    /**
     * media name combiner
     */
    private static final BinaryOperator<String> NAME_COMBINER = (path, name) -> {
        int index = lastIndexOf(name, ".");
        return path + "\\" + name.substring(0, index) +
                "-" + randomAlphabetic(RANDOM_FILE_NAME_POST_LEN) +
                "-" + currentTimeMillis() +
                name.substring(index);
    };

    private static final Function<String, FileChannel> CHANNEL_GEN = descName -> {
        try {
            return open(new File(descName).toPath(), CREATE_NEW, WRITE);
        } catch (Exception e) {
            LOGGER.error("CHANNEL_GEN failed, descName = {}, e = {}", descName, e);
            throw new BlueException(BAD_REQUEST);
        }
    };

    private final Supplier<Monitor<Long>> MONITOR_SUP = () ->
            new Monitor<>(0L, Long::sum, m -> m <= singleFileSizeThreshold);

    private final BiFunction<Part, FileChannel, Mono<Long>> PART_FILE_WRITER = (part, channel) -> {
        FilePart filePart = (FilePart) part;
        Flux<DataBuffer> dataBufferFlux = filePart.content();
        Monitor<Long> monitor = MONITOR_SUP.get();

        return dataBufferFlux
                .onBackpressureBuffer(BACKPRESSURE_BUFFER_MAX_SIZE, ERROR)
                .doOnNext(dataBuffer -> {
                    if (monitor.operateWithAssert((long) dataBuffer.readableByteCount())) {
                        try {
                            channel.write(dataBuffer.asByteBuffer());
                        } catch (Exception e) {
                            throw new RuntimeException("upload failed, e = " + e);
                        }
                    } else {
                        throw new BlueException(PAYLOAD_TOO_LARGE);
                    }
                }).collectList()
                .flatMap(v ->
                        just(monitor.getMonitored()));
    };

    private final BiFunction<byte[], FileChannel, Mono<Long>> BYTE_FILE_WRITER = (bytes, channel) -> {
        Monitor<Long> monitor = MONITOR_SUP.get();
        return BUFFER_FLUX_CONVERTER.apply(bytes)
                .onBackpressureBuffer(BACKPRESSURE_BUFFER_MAX_SIZE, ERROR)
                .doOnNext(dataBuffer -> {
                    if (monitor.operateWithAssert((long) dataBuffer.readableByteCount())) {
                        try {
                            channel.write(dataBuffer.asByteBuffer());

                            release(dataBuffer);
                        } catch (Exception e) {
                            throw new RuntimeException("upload failed, e = " + e);
                        }
                    } else {
                        throw new BlueException(PAYLOAD_TOO_LARGE);
                    }
                }).collectList()
                .flatMap(v ->
                        just(monitor.getMonitored()));
    };

    private final Consumer<FileChannel> CHANNEL_CLOSER = channel -> {
        try {
            channel.close();
        } catch (Exception e) {
            LOGGER.error("FileChannel close failed, e = {}", e.getMessage());
        }
    };

    private final BiFunction<String, Long, Flux<DataBuffer>> DATA_BUFFERS_READER = (link, memberId) -> {
        if (isBlank(link) || isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        try {
            return readByteChannel(() -> open(new File(link).toPath(), READ), DATA_BUFFER_FACTORY, BUFFER_SIZE);
        } catch (Exception e) {
            throw new BlueException(BAD_REQUEST);
        }
    };

    /**
     * write for upload
     *
     * @param part
     * @param type
     * @param memberId
     * @return
     */
    @Override
    public Mono<FileUploadResult> write(Part part, Integer type, Long memberId) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);
        assertAttachmentType(type, false);

        String name = PART_NAME_GETTER.apply(part);
        String descName = NAME_COMBINER.apply(descPath, name);

        return using(
                () -> CHANNEL_GEN.apply(descName),
                ch -> PART_FILE_WRITER.apply(part, ch),
                CHANNEL_CLOSER,
                true)
                .flatMap(size ->
                        just(new FileUploadResult(type, descName, name, true, SUCCESS_MSG, size))
                );
    }

    /**
     * write
     *
     * @param bytes
     * @param type
     * @param memberId
     * @param originalName
     * @param descName
     * @return
     */
    @Override
    public Mono<FileUploadResult> write(byte[] bytes, Integer type, Long memberId, String originalName, String descName) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);
        assertAttachmentType(type, false);

        if (isBlank(descName))
            throw new BlueException(BAD_REQUEST);

        return using(
                () -> CHANNEL_GEN.apply(descName),
                ch -> BYTE_FILE_WRITER.apply(bytes, ch),
                CHANNEL_CLOSER,
                true)
                .flatMap(size ->
                        just(new FileUploadResult(type, descName, isNotBlank(originalName) ? originalName : EMPTY_VALUE.value, true, SUCCESS_MSG, size))
                );
    }

    /**
     * read for download
     *
     * @param link
     * @param memberId
     * @return
     */
    @Override
    public Flux<DataBuffer> read(String link, Long memberId) {
        return DATA_BUFFERS_READER.apply(link, memberId);
    }

    /**
     * byte handler type
     *
     * @return
     */
    @Override
    public ByteHandlerType handlerType() {
        return LOCAL_DISK;
    }

}