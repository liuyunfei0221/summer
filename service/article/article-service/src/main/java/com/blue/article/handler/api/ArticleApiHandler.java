package com.blue.article.handler.api;

import com.blue.article.model.ArticleInsertParam;
import com.blue.article.service.inter.ControlService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * article api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class ArticleApiHandler {

    private final ControlService controlService;

    public ArticleApiHandler(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * insert article
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertArticle(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ArticleInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    controlService.insertArticle(tuple2.getT1(), tuple2.getT2().getId());
                    return just(true);
                })
                .flatMap(b ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(b, serverRequest), BlueResponse.class)
                );
    }


}
