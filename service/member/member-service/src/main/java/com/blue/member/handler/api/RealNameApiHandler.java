package com.blue.member.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.model.RealNameUpdateParam;
import com.blue.member.service.inter.RealNameControlService;
import com.blue.member.service.inter.RealNameService;
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
 * real name api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RealNameApiHandler {

    private final RealNameService realNameService;

    private final RealNameControlService realNameControlService;

    public RealNameApiHandler(RealNameService realNameService, RealNameControlService realNameControlService) {
        this.realNameService = realNameService;
        this.realNameControlService = realNameControlService;
    }

    /**
     * get real name info by access
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        realNameControlService.getRealNameInfoByMemberIdWithAssert(acc.getId())
                                .flatMap(mri ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(success(mri, serverRequest), BlueResponse.class))
                );
    }

    /**
     * update real name
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(RealNameUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        just(realNameService.updateRealName(tuple2.getT1().getId(), tuple2.getT2())))
                .flatMap(mri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mri, serverRequest), BlueResponse.class));
    }

}
