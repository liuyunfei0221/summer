package com.blue.media.service.inter;

import com.blue.media.api.model.FileUploadResult;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * byte operate service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface ByteOperateService {

    /**
     * upload
     *
     * @param resources
     * @param type
     * @param memberId
     * @return
     */
    Mono<List<FileUploadResult>> upload(List<Part> resources, Integer type, Long memberId);

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> upload(ServerRequest serverRequest);

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> download(ServerRequest serverRequest);

}
