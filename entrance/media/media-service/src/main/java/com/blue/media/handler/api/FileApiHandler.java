package com.blue.media.handler.api;

import com.blue.media.service.inter.ByteOperateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;


/**
 * media api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public final class FileApiHandler {

    private static final Logger LOGGER = getLogger(FileApiHandler.class);

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
