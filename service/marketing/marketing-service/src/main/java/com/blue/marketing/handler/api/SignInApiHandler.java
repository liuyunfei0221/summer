package com.blue.marketing.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.marketing.service.inter.SignInService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
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
                                                .body(success(r, serverRequest), BlueResponse.class))
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
                                .flatMap(i ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(success(i, serverRequest), BlueResponse.class))

                );
    }

}
