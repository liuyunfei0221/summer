package com.blue.member.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.service.inter.MemberRegistryService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * member registry handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberRegistryApiHandler {

    private final MemberRegistryService memberRegistryService;

    public MemberRegistryApiHandler(MemberRegistryService memberRegistryService) {
        this.memberRegistryService = memberRegistryService;
    }

    /**
     * registry member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> registry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberRegistryParam.class)
                .switchIfEmpty(
                        error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(mrp ->
                        just(memberRegistryService.registerMemberBasic(mrp))
                )
                .flatMap(mi ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mi, serverRequest), BlueResponse.class));
    }


}
