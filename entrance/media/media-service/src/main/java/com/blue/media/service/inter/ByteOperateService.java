package com.blue.media.service.inter;

import com.blue.media.api.model.AttachmentUploadInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
     * @param bytes
     * @param type
     * @param memberId
     * @param originalName
     * @param descName
     * @return
     */
    Mono<AttachmentUploadInfo> upload(byte[] bytes, Integer type, Long memberId, String originalName, String descName);

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
