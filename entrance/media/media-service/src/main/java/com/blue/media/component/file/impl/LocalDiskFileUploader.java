package com.blue.media.component.file.impl;

import com.blue.base.common.base.Monitor;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.inter.FileUploader;
import com.blue.media.config.deploy.FileDeploy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static reactor.core.publisher.BufferOverflowStrategy.ERROR;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.using;
import static reactor.util.Loggers.getLogger;


/**
 * uploader implements
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public final class LocalDiskFileUploader implements FileUploader {

    private static final Logger LOGGER = getLogger(LocalDiskFileUploader.class);

    public LocalDiskFileUploader(FileDeploy fileDeploy) {
        validTypes = new HashSet<>(fileDeploy.getValidTypes());
        invalidPres = new HashSet<>(fileDeploy.getInvalidPres());
        nameLenThreshold = fileDeploy.getNameLenThreshold();
        descPath = fileDeploy.getDescPath();
        singleFileSizeThreshold = fileDeploy.getSingleFileSizeThreshold();
    }

    private static final int RANDOM_FILE_NAME_POST_LEN = 8;

    private static final int BACKPRESSURE_BUFFER_MAX_SIZE = 128;

    private Set<String> validTypes;

    private Set<Character> invalidPres;

    private long singleFileSizeThreshold;

    private int nameLenThreshold;

    private final String descPath;

    /**
     * assert media
     */
    private final Function<Part, String> PART_NAME_GETTER = part -> {
        if (part == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "part can't be empty");

        FilePart filePart = (FilePart) part;
        String fileName = filePart.filename();
        if ("".equals(fileName))
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

    private static final Function<String, FileChannel> WRITE_CHANNEL_GEN = descName -> {
        try {
            return FileChannel.open(new File(descName).toPath(), CREATE_NEW, WRITE);
        } catch (Exception e) {
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    @SuppressWarnings("ConstantConditions")
    private final Supplier<Monitor<Long>> MONITOR_SUP = () ->
            new Monitor<>(0L, Long::sum, m -> m <= singleFileSizeThreshold);

    private final BiFunction<Part, FileChannel, Mono<Long>> DATA_WRITER = (part, channel) -> {
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

    private final Consumer<FileChannel> STREAM_CLOSER = stream -> {
        try {
            stream.close();
        } catch (Exception e) {
            LOGGER.error("FastByteArrayOutputStream close failed, e = {}", e);
        }
    };

    /**
     * upload media
     *
     * @param part
     * @return
     */
    @Override
    public Mono<FileUploadResult> upload(Part part) {
        String name = PART_NAME_GETTER.apply(part);
        String descName = NAME_COMBINER.apply(descPath, name);

        return using(() -> WRITE_CHANNEL_GEN.apply(descName),
                ch -> DATA_WRITER.apply(part, ch),
                STREAM_CLOSER,
                true)
                .flatMap(size ->
                        just(new FileUploadResult(descName, name, true, "upload success", size))
                );
    }

}
