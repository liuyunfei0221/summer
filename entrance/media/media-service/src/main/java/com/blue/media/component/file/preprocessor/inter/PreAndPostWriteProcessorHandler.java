package com.blue.media.component.file.preprocessor.inter;

import com.blue.basic.constant.media.AttachmentType;
import com.blue.media.api.model.FileUploadResult;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

/**
 * byte operate pre and post handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface PreAndPostWriteProcessorHandler {

    /**
     * handle part before write
     *
     * @param part
     * @param memberId
     * @return
     */
    Mono<Part> preHandle(Part part, Long memberId);

    /**
     * handle part before write
     *
     * @param bytes
     * @param memberId
     * @param originalName
     * @param descName
     * @return
     */
    Mono<byte[]> preHandle(byte[] bytes, Long memberId, String originalName, String descName);

    /**
     * handle part after write
     *
     * @param fileUploadResult
     * @param memberId
     * @return
     */
    Mono<FileUploadResult> postHandle(FileUploadResult fileUploadResult, Long memberId);

    /**
     * handle attachment type
     *
     * @return
     */
    AttachmentType handlerType();

}
