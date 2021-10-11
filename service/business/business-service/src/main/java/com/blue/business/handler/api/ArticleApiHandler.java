package com.blue.business.handler.api;

import com.blue.base.model.base.Access;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.business.api.model.ArticleInsertParam;
import com.blue.business.service.inter.BusinessService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccess;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

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
        Access access = getAccess(serverRequest);
        return serverRequest.bodyToMono(ArticleInsertParam.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(ai -> {
                    businessService.insertArticle(ai, access.getId());
                    return just(true);
                })
                .flatMap(b ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, b, OK.message), BlueResponse.class)
                );
    }


}
