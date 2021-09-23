package com.blue.member.handler.api;

import com.blue.base.model.base.BlueResult;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryInfo;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_REQUEST_BODY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * 用户api接口
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
     * 根据用户session信息获取用户
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getMember(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        memberBasicService.getMemberInfoByPrimaryKeyWithAssert(ai.getId())
                                .flatMap(mv ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mv, OK.message), BlueResult.class))
                );
    }

    /**
     * 注册
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> registry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberRegistryInfo.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_REQUEST_BODY.message)))
                .flatMap(mr -> {
                    memberBasicService.insert(mr);
                    return just(true);
                })
                .flatMap(b ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, b, OK.message), BlueResult.class)
                );
    }


}
