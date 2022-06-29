package com.blue.marketing.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.marketing.service.inter.SignInService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * sign in api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class SignInApiHandler {

    private final SignInService signInService;

    public SignInApiHandler(SignInService signInService) {
        this.signInService = signInService;
    }

    /**
     * sign in
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> signIn(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        signInService.signIn(acc.getId())
                                .flatMap(r ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, r, serverRequest), BlueResponse.class))
                );
    }

    /**
     * query sign in record
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectRecords(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        signInService.getSignInRecord(acc.getId())
                                .flatMap(info ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, info, serverRequest), BlueResponse.class))

                );
    }

}
