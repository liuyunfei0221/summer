package com.blue.marketing.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.marketing.service.inter.SignService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * 签到接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class SignApiHandler {

    private final SignService signService;

    public SignApiHandler(SignService signService) {
        this.signService = signService;
    }

    /**
     * 签到
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> signIn(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        signService.insertSignIn(ai.getId())
                                .flatMap(r ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, r, OK.message), BlueResponse.class))
                );
    }

    /**
     * 查询当月签到
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getSignInRecord(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        signService.getSignInRecord(ai.getId())
                                .flatMap(info ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, info, OK.message), BlueResponse.class))

                );
    }

}
