package com.blue.file.config.common;

import com.blue.base.common.base.Monitor;
import com.blue.base.common.multitask.BlueProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.FileUploadResult;
import com.blue.file.api.model.FileValidResult;
import com.blue.file.config.deploy.FileDeploy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.util.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static reactor.util.Loggers.getLogger;


/**
 * 文件上传处理函数
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnusedAssignment", "AliControlFlowStatementWithoutBraces"})
public final class FileProcessor implements BlueProcessor<Part, FileUploadResult> {

    private static final Logger LOGGER = getLogger(FileProcessor.class);

    public FileProcessor(FileDeploy fileDeploy) {
        VALID_TYPES = new HashSet<>(fileDeploy.getValidTypes());
        INVALID_PRES = new HashSet<>(fileDeploy.getInvalidPres());
        NAME_LEN_THRESHOLD = fileDeploy.getNameLenThreshold();
        DESC_PATH = fileDeploy.getDescPath();
        SINGLE_FILE_SIZE_THRESHOLD = fileDeploy.getSingleFileSizeThreshold();
    }

    private static final int RANDOM_FILE_NAME_POST_LEN = 8;

    private static Set<String> VALID_TYPES;

    private static Set<Character> INVALID_PRES;

    private static long SINGLE_FILE_SIZE_THRESHOLD;

    private static int NAME_LEN_THRESHOLD;

    private static String DESC_PATH;

    /**
     * 文件校验
     */
    private static final Function<Part, FileValidResult> PART_VALIDATOR = part -> {
        if (part == null)
            return new FileValidResult(false, "", "文件为空");

        FilePart filePart = (FilePart) part;
        String fileName = filePart.filename();
        if ("".equals(fileName))
            return new FileValidResult(false, fileName, "文件的全文件名为空");

        int nameLength = fileName.length();
        if (nameLength > NAME_LEN_THRESHOLD)
            return new FileValidResult(false, fileName, "文件(" + fileName + ")的全文件名长度超过" + NAME_LEN_THRESHOLD);

        int index = lastIndexOf(fileName, SCHEME_SEPARATOR.identity);
        if (index == -1 || nameLength - 1 == index)
            return new FileValidResult(false, fileName, "文件(" + fileName + ")为未识别的文件格式");

        String postName = fileName.substring(index).toUpperCase();
        if (!VALID_TYPES.contains(postName))
            return new FileValidResult(false, fileName, "文件(" + fileName + ")的文件格式非法");

        String preName = fileName.substring(0, index);
        int preLength = preName.length();
        for (int i = 0; i < preLength; i++) {
            char c = preName.charAt(i);
            if (INVALID_PRES.contains(c))
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
     * 文件处理
     */
    private final Function<Part, FileUploadResult> PROCESSOR = part -> {
        FileValidResult result = PART_VALIDATOR.apply(part);
        if (!result.getValid())
            return new FileUploadResult("", result.getName(), false, result.getMessage(), 0L);

        FilePart filePart = (FilePart) part;
        String descName = NAME_COMBINER.apply(DESC_PATH, result.getName());
        File desc = new File(descName);

        @SuppressWarnings("ConstantConditions")
        Monitor<Long> monitor = new Monitor<>(0L, Long::sum, m -> m <= SINGLE_FILE_SIZE_THRESHOLD);
        Flux<DataBuffer> dataBufferFlux = filePart.content();

        try (FileOutputStream descOutputStream = new FileOutputStream(desc);
             BufferedOutputStream descBufferedOutputStream = new BufferedOutputStream(descOutputStream)) {
            dataBufferFlux.onErrorStop().subscribe(dataBuffer -> {
                byte[] array = dataBuffer.asByteBuffer().array();
                if (monitor.operateWithAssert((long) array.length)) {
                    try {
                        descBufferedOutputStream.write(array);
                    } catch (IOException e) {
                        LOGGER.error("文件上传失败 e -> " + e);
                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "上传失败");
                    }
                } else {
                    throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "文件大小不能超过" + monitor.getMonitored());
                }
                array = null;
            });
            return new FileUploadResult(descName, result.getName(), true, "上传成功", monitor.getMonitored());
        } catch (BlueException e) {
            LOGGER.error("文件上传失败 e -> " + e);
            return new FileUploadResult(descName, result.getName(), false, e.getCause().getMessage(), monitor.getMonitored());
        } catch (Exception e) {
            LOGGER.error("文件上传失败 e -> " + e);
            return new FileUploadResult(descName, result.getName(), false, "上传失败", monitor.getMonitored());
        }
    };

    /**
     * 处理成功
     *
     * @return
     */
    @Override
    public Function<Part, FileUploadResult> processor() {
        return PROCESSOR;
    }

    /**
     * 失败回调
     */
    private final BiFunction<Part, Throwable, FileUploadResult> FAIL_CALLBACK = (part, ex) -> {
        String name = "";
        try {
            name = part.name();
        } catch (Exception e) {
            LOGGER.error("task failed: ", e);
        } finally {
            part = null;
        }
        return new FileUploadResult("", name, false, "task failed", 0L);
    };

    /**
     * 处理失败
     *
     * @return
     */
    @Override
    public BiFunction<Part, Throwable, FileUploadResult> failProcessor() {
        return FAIL_CALLBACK;
    }
}
