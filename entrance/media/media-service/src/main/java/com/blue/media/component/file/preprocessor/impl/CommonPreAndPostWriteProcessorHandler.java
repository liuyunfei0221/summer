package com.blue.media.component.file.preprocessor.impl;

import com.blue.basic.constant.media.AttachmentType;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.preprocessor.inter.PreAndPostWriteProcessorHandler;
import org.slf4j.Logger;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

import static com.blue.basic.constant.media.AttachmentType.COMMON;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

/**
 * common attachment operate pre and post corona
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class CommonPreAndPostWriteProcessorHandler implements PreAndPostWriteProcessorHandler {

    private static final Logger LOGGER = getLogger(CommonPreAndPostWriteProcessorHandler.class);

    /**
     * handle part before write
     *
     * @param part
     * @param memberId
     * @return
     */
    @Override
    public Mono<Part> preHandle(Part part, Long memberId) {
        LOGGER.info("part = {}, memberId = {}", part, memberId);
        return just(part);
    }

    /**
     * handle part before write
     *
     * @param bytes
     * @param memberId
     * @param originalName
     * @param descName
     * @return
     */
    @Override
    public Mono<byte[]> preHandle(byte[] bytes, Long memberId, String originalName, String descName) {
        LOGGER.info("memberId = {}, originalName = {}, descName = {}", memberId, originalName, descName);
        return just(bytes);
    }

    /**
     * handle part after write
     *
     * @param fileUploadResult
     * @param memberId
     * @return
     */
    @Override
    public Mono<FileUploadResult> postHandle(FileUploadResult fileUploadResult, Long memberId) {
        LOGGER.info("fileUploadResult = {}, memberId = {}", fileUploadResult, memberId);
        return just(fileUploadResult);
    }

    /**
     * handle attachment type
     *
     * @return
     */
    @Override
    public AttachmentType handlerType() {
        return COMMON;
    }

}