package com.blue.media.component.file.inter;

import com.blue.base.constant.media.ByteHandlerType;
import com.blue.media.api.model.FileUploadResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * byte operate processor
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface ByteHandler {

    /**
     * write
     *
     * @param part
     * @param memberId
     * @return
     */
    Mono<FileUploadResult> write(Part part, Long memberId);

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
