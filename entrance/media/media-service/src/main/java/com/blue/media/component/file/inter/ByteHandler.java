package com.blue.media.component.file.inter;

import com.blue.base.constant.media.ByteHandlerType;
import com.blue.media.api.model.FileUploadResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

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
     * @return
     */
    Mono<FileUploadResult> write(Part part);

    /**
     * read
     *
     * @param path
     * @return
     */
    Flux<DataBuffer> read(Path path);

    /**
     * byte handler type
     *
     * @return
     */
    ByteHandlerType handlerType();

}
