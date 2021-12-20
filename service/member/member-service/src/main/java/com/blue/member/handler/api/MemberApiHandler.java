package com.blue.member.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * member api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberApiHandler {

    private final MemberBasicService memberBasicService;

    public MemberApiHandler(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * get member info by access
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectMemberInfo(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        memberBasicService.selectMemberInfoMonoByPrimaryKeyWithAssert(ai.getId())
                                .flatMap(mv ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mv, OK.message), BlueResponse.class))
                );
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
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(mr ->
                        just(memberBasicService.insertMemberBasic(mr))
                )
                .flatMap(mi ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mi, OK.message), BlueResponse.class));
    }


}
