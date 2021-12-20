package com.blue.business.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.business.model.ArticleInsertParam;
import com.blue.business.service.inter.BusinessService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * article api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public class ArticleApiHandler {

    private final BusinessService businessService;

    public ArticleApiHandler(BusinessService businessService) {
        this.businessService = businessService;
    }

    /**
     * insert article
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertArticle(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ArticleInsertParam.class)
                        .switchIfEmpty(error(new BlueException(EMPTY_PARAM.status, EMPTY_PARAM.code, EMPTY_PARAM.message))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    businessService.insertArticle(tuple2.getT1(), tuple2.getT2().getId());
                    return just(true);
                })
                .flatMap(b ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, b, OK.message), BlueResponse.class)
                );
    }


}
