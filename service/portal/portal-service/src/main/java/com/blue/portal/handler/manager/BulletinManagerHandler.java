package com.blue.portal.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.portal.constant.PortalTypeReference.PAGE_MODEL_FOR_BULLETIN_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * bulletin manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class BulletinManagerHandler {

    private final BulletinService bulletinService;

    public BulletinManagerHandler(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    /**
     * select bulletin by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> listBulletin(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_BULLETIN_CONDITION_TYPE)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(bulletinService::selectBulletinInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
