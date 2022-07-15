package com.blue.member.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.model.RealNameUpdateParam;
import com.blue.member.service.inter.RealNameService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
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

    public RealNameApiHandler(RealNameService realNameService) {
        this.realNameService = realNameService;
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
                        realNameService.getRealNameInfoMonoByMemberIdWithAssert(acc.getId())
                                .flatMap(mdi ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mdi, serverRequest), BlueResponse.class))
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
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

}
