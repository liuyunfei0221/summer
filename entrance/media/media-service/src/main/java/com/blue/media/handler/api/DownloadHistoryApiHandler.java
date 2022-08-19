package com.blue.media.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.media.constant.MediaTypeReference.SCROLL_MODEL_FOR_DOWNLOAD_HISTORY_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * download history api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class DownloadHistoryApiHandler {

    private final DownloadHistoryService downloadHistoryService;

    public DownloadHistoryApiHandler(DownloadHistoryService downloadHistoryService) {
        this.downloadHistoryService = downloadHistoryService;
    }

    /**
     * select download history by scroll and current member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scroll(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(SCROLL_MODEL_FOR_DOWNLOAD_HISTORY_TYPE)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        downloadHistoryService.selectShineInfoScrollMonoByScrollAndCursorBaseOnMemberId(tuple2.getT1(), tuple2.getT2().getId())
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
