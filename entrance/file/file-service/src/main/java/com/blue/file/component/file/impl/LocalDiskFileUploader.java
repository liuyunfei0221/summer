package com.blue.file.component.file.impl;

import com.blue.base.common.base.Monitor;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.FileUploadResult;
import com.blue.file.api.model.FileValidResult;
import com.blue.file.component.file.inter.FileUploader;
import com.blue.file.config.deploy.FileDeploy;
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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static reactor.core.publisher.BufferOverflowStrategy.ERROR;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * 文件上传处理函数
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
     * 文件校验
     */
    private final Function<Part, FileValidResult> PART_VALIDATOR = part -> {
        if (part == null)
            return new FileValidResult(false, "", "文件为空");

        FilePart filePart = (FilePart) part;
        String fileName = filePart.filename();
        if ("".equals(fileName))
            return new FileValidResult(false, fileName, "文件的全文件名为空");

        int nameLength = fileName.length();
        if (nameLength > nameLenThreshold)
            return new FileValidResult(false, fileName, "文件(" + fileName + ")的全文件名长度超过" + nameLenThreshold);

        int index = lastIndexOf(fileName, SCHEME_SEPARATOR.identity);
        if (index == -1 || nameLength - 1 == index)
            return new FileValidResult(false, fileName, "文件(" + fileName + ")为未识别的文件格式");

        String postName = fileName.substring(index).toUpperCase();
        if (!validTypes.contains(postName))
            return new FileValidResult(false, fileName, "文件(" + fileName + ")的文件格式非法");

        String preName = fileName.substring(0, index);
        int preLength = preName.length();
        for (int i = 0; i < preLength; i++) {
            char c = preName.charAt(i);
            if (invalidPres.contains(c))
                return new FileValidResult(false, fileName, "文件(" + fileName + ")的前缀文件名包含非法字符(" + c + ")");
        }

        return new FileValidResult(true, fileName, "校验通过");
    };

    /**
     * 文件名处理
     */
    private static final BinaryOperator<String> NAME_COMBINER = (path, name) -> {
        int index = lastIndexOf(name, ".");
        return path + "\\" + name.substring(0, index) +
                "-" + randomAlphabetic(RANDOM_FILE_NAME_POST_LEN) +
                "-" + currentTimeMillis() +
                name.substring(index);
    };

    /**
     * 文件上传
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
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("资源释放失败, e = {}", e);
                }
            };

            return dataBufferFlux
                    .onErrorStop()
                    .doOnComplete(resourceCloser)
                    .doOnCancel(resourceCloser)
                    .doOnError(throwable -> {
                        LOGGER.error("文件上传失败, throwable -> " + throwable);
                        resourceCloser.run();
                    })
                    .onBackpressureBuffer(BACKPRESSURE_BUFFER_MAX_SIZE, ERROR)
                    .doOnNext(dataBuffer -> {
                        byte[] array = dataBuffer.asByteBuffer().array();
                        if (monitor.operateWithAssert((long) array.length)) {
                            try {
                                bufferedOutputStream.write(array);
                            } catch (IOException e) {
                                LOGGER.error("文件上传失败 e -> " + e);
                                error(new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "上传失败"));
                            }
                        } else {
                            error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "文件大小不能超过" + monitor.getMonitored()));
                        }
                        array = null;
                    }).collectList()
                    .flatMap(v ->
                            just(new FileUploadResult(descName, validResult.getName(), true, "上传成功", monitor.getMonitored())));

        } catch (BlueException e) {
            LOGGER.error("文件上传失败 e -> " + e);
            return just(new FileUploadResult(descName, validResult.getName(), false, e.getCause().getMessage(), monitor.getMonitored()));
        } catch (Exception e) {
            LOGGER.error("文件上传失败 e -> " + e);
            return just(new FileUploadResult(descName, validResult.getName(), false, "上传失败", monitor.getMonitored()));
        }
    }

}
