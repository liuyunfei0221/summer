package com.blue.media.component.file.preprocessor.inter;

import com.blue.base.constant.media.AttachmentType;
import com.blue.media.api.model.FileUploadResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
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
     * @param type
     * @param memberId
     * @return
     */
    Part preHandle(Part part, Integer type, Long memberId);

    /**
     * handle part after write
     *
     * @param fileUploadResult
     * @param type
     * @param memberId
     * @return
     */
    Mono<FileUploadResult> postHandle(FileUploadResult fileUploadResult, Integer type, Long memberId);

    /**
     * read
     *
     * @param link
     * @param memberId
     * @return
     */
    Flux<DataBuffer> read(String link, Long memberId);

    /**
     * handle attachment type
     *
     * @return
     */
    AttachmentType handlerType();

}
