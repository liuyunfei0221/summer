package com.blue.media.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * download history manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class DownloadHistoryManagerHandler {

    private final DownloadHistoryService downloadHistoryService;

    public DownloadHistoryManagerHandler(DownloadHistoryService downloadHistoryService) {
        this.downloadHistoryService = downloadHistoryService;
    }

    /**
     * select download history by page and condition
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listDownloadHistory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(pageModelRequest ->
                        (Mono<PageModelResponse<DownloadHistoryInfo>>) downloadHistoryService.selectDownloadHistoryInfoPageMonoByPageAndCondition(pageModelRequest)
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
