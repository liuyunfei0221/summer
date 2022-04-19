package com.blue.media.handler.api;

import com.blue.media.service.inter.ByteOperateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * media api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public final class FileApiHandler {

    private final ByteOperateService byteOperateService;

    public FileApiHandler(ByteOperateService byteOperateService) {
        this.byteOperateService = byteOperateService;
    }

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        return byteOperateService.upload(serverRequest);
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        return byteOperateService.download(serverRequest);
    }

}
