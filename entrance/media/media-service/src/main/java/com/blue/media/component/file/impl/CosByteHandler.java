package com.blue.media.component.file.impl;

import com.blue.base.common.base.Monitor;
import com.blue.base.constant.media.ByteHandlerType;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.inter.ByteHandler;
import com.blue.media.config.deploy.LocalDiskFileDeploy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.common.Symbol.SCHEME_SEPARATOR;
import static com.blue.base.constant.media.ByteHandlerType.COS;
import static com.blue.media.common.MediaCommonFunctions.BUFFER_SIZE;
import static com.blue.media.common.MediaCommonFunctions.DATA_BUFFER_FACTORY;
import static java.lang.System.currentTimeMillis;
import static java.nio.channels.FileChannel.open;
import static java.nio.file.StandardOpenOption.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.springframework.core.io.buffer.DataBufferUtils.readByteChannel;
import static reactor.core.publisher.BufferOverflowStrategy.ERROR;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.using;
import static reactor.util.Loggers.getLogger;


/**
 * local disk byte operate processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class CosByteHandler implements ByteHandler {

    private static final Logger LOGGER = getLogger(CosByteHandler.class);

    public CosByteHandler(LocalDiskFileDeploy localDiskFileDeploy) {
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

        int index = lastIndexOf(fileName, SCHEME_SEPARATOR.identity);
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
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    private final Supplier<Monitor<Long>> MONITOR_SUP = () ->
            new Monitor<>(0L, Long::sum, m -> m <= singleFileSizeThreshold);

    private final BiFunction<Part, FileChannel, Mono<Long>> FILE_WRITER = (part, channel) -> {
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
                            LOGGER.error("upload failed, e -> " + e);
                            throw new BlueException(INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        throw new BlueException(INTERNAL_SERVER_ERROR);
                    }
                }).collectList()
                .flatMap(v ->
                        just(monitor.getMonitored()));
    };

    private final Consumer<FileChannel> CHANNEL_CLOSER = channel -> {
        try {
            channel.close();
        } catch (Exception e) {
            LOGGER.error("FileChannel close failed, e = {}", e);
        }
    };

    private final BiFunction<String, Long, Flux<DataBuffer>> DATA_BUFFERS_READER = (link, memberId) -> {
        if (isBlank(link) || isInvalidIdentity(memberId))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        try {
            return readByteChannel(() -> open(new File(link).toPath(), READ), DATA_BUFFER_FACTORY, BUFFER_SIZE);
        } catch (Exception e) {
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    /**
     * write for upload
     *
     * @param part
     * @param memberId
     * @return
     */
    @Override
    public Mono<FileUploadResult> write(Part part, Long memberId) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        String name = PART_NAME_GETTER.apply(part);
        String descName = NAME_COMBINER.apply(descPath, name);

        return using(
                () -> CHANNEL_GEN.apply(descName),
                ch -> FILE_WRITER.apply(part, ch),
                CHANNEL_CLOSER,
                true)
                .flatMap(size ->
                        just(new FileUploadResult(descName, name, true, SUCCESS_MSG, size))
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
        return COS;
    }

}
