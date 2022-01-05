package com.blue.media.component.file.impl;

import com.blue.base.common.base.Monitor;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.api.model.FileValidResult;
import com.blue.media.component.file.inter.FileUploader;
import com.blue.media.config.deploy.FileDeploy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.PAYLOAD_TOO_LARGE;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static reactor.core.publisher.BufferOverflowStrategy.ERROR;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * uploader implements
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnusedAssignment", "AliControlFlowStatementWithoutBraces"})
@Component
public final class LocalDiskFileUploader implements FileUploader {

    private static final Logger LOGGER = getLogger(LocalDiskFileUploader.class);

    public LocalDiskFileUploader(FileDeploy fileDeploy) {
        validTypes = new HashSet<>(fileDeploy.getValidTypes());
        invalidPres = new HashSet<>(fileDeploy.getInvalidPres());
        nameLenThreshold = fileDeploy.getNameLenThreshold();
        DESC_PATH = fileDeploy.getDescPath();
        SINGLE_FILE_SIZE_THRESHOLD = fileDeploy.getSingleFileSizeThreshold();
    }

    private static final int RANDOM_FILE_NAME_POST_LEN = 8;

    private static final int BACKPRESSURE_BUFFER_MAX_SIZE = 128;

    private Set<String> validTypes;

    private Set<Character> invalidPres;

    private final long SINGLE_FILE_SIZE_THRESHOLD;

    private int nameLenThreshold;

    private final String DESC_PATH;

    /**
     * assert media
     */
    private final Function<Part, FileValidResult> PART_VALIDATOR = part -> {
        if (part == null)
            return new FileValidResult(false, "", "part can't be empty");

        FilePart filePart = (FilePart) part;
        String fileName = filePart.filename();
        if ("".equals(fileName))
            return new FileValidResult(false, fileName, "media name can't be blank");

        int nameLength = fileName.length();
        if (nameLength > nameLenThreshold)
            return new FileValidResult(false, fileName, "media (" + fileName + ") name length can't be greater than " + nameLenThreshold);

        int index = lastIndexOf(fileName, SCHEME_SEPARATOR.identity);
        if (index == -1 || nameLength - 1 == index)
            return new FileValidResult(false, fileName, "media (" + fileName + ") has a unknown type");

        String postName = fileName.substring(index).toUpperCase();
        if (!validTypes.contains(postName))
            return new FileValidResult(false, fileName, "media (" + fileName + ") type is invalid");

        String preName = fileName.substring(0, index);
        int preLength = preName.length();
        for (int i = 0; i < preLength; i++) {
            char c = preName.charAt(i);
            if (invalidPres.contains(c))
                return new FileValidResult(false, fileName, "media (" + fileName + ") name's prefix contains invalid str (" + c + ")");
        }

        return new FileValidResult(true, fileName, "access");
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

    /**
     * upload media
     *
     * @param part
     * @return
     */
    @Override
    public Mono<FileUploadResult> upload(Part part) {
        FileValidResult validResult = PART_VALIDATOR.apply(part);
        if (!validResult.getValid())
            return just(new FileUploadResult("", validResult.getName(), false, validResult.getMessage(), 0L));

        FilePart filePart = (FilePart) part;
        String descName = NAME_COMBINER.apply(DESC_PATH, validResult.getName());
        File desc = new File(descName);

        @SuppressWarnings("ConstantConditions")
        Monitor<Long> monitor = new Monitor<>(0L, Long::sum, m -> m <= SINGLE_FILE_SIZE_THRESHOLD);
        Flux<DataBuffer> dataBufferFlux = filePart.content();

        try {
            FileOutputStream descOutputStream = new FileOutputStream(desc);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(descOutputStream);

            Runnable resourceCloser = () -> {
                try {
                    bufferedOutputStream.close();
                    descOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("resources release failed, e = {}", e);
                }
            };

            return dataBufferFlux
                    .onErrorStop()
                    .doOnComplete(resourceCloser)
                    .doOnCancel(resourceCloser)
                    .doOnError(throwable -> {
                        LOGGER.error("upload failed, throwable -> " + throwable);
                        resourceCloser.run();
                    })
                    .onBackpressureBuffer(BACKPRESSURE_BUFFER_MAX_SIZE, ERROR)
                    .doOnNext(dataBuffer -> {
                        byte[] array = dataBuffer.asByteBuffer().array();
                        if (monitor.operateWithAssert((long) array.length)) {
                            try {
                                bufferedOutputStream.write(array);
                            } catch (IOException e) {
                                LOGGER.error("upload failed, e -> " + e);
                                error(() -> new BlueException(INTERNAL_SERVER_ERROR));
                            }
                        } else {
                            error(() -> new BlueException(PAYLOAD_TOO_LARGE));
                        }
                        array = null;
                    }).collectList()
                    .flatMap(v ->
                            just(new FileUploadResult(descName, validResult.getName(), true, "upload success", monitor.getMonitored())));

        } catch (BlueException e) {
            LOGGER.error("upload failed, e -> " + e);
            return just(new FileUploadResult(descName, validResult.getName(), false, e.getCause().getMessage(), monitor.getMonitored()));
        } catch (Exception e) {
            LOGGER.error("upload failed, e -> " + e);
            return just(new FileUploadResult(descName, validResult.getName(), false, "upload failed", monitor.getMonitored()));
        }
    }

}
