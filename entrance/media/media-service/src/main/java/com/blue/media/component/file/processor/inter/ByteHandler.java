package com.blue.media.component.file.processor.inter;

import com.blue.base.constant.media.ByteHandlerType;
import com.blue.media.api.model.FileUploadResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * byte operate handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface ByteHandler {

    /**
     * write
     *
     * @param part
     * @param type
     * @param memberId
     * @return
     */
    Mono<FileUploadResult> write(Part part, Integer type, Long memberId);

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
    Mono<FileUploadResult> write(byte[] bytes, Integer type, Long memberId, String originalName, String descName);

    /**
     * read
     *
     * @param link
     * @param memberId
     * @return
     */
    Flux<DataBuffer> read(String link, Long memberId);

    /**
     * byte handler type
     *
     * @return
     */
    ByteHandlerType handlerType();

}
