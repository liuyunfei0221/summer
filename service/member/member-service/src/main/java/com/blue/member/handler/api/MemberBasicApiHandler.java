package com.blue.member.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.common.IdentityParam;
import com.blue.base.model.common.StringDataParam;
import com.blue.base.model.exps.BlueException;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * member api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberBasicApiHandler {

    private final MemberBasicService memberBasicService;

    public MemberBasicApiHandler(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * get member info by access
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getByAccess(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        memberBasicService.getMemberBasicInfoMonoWithAssert(ai.getId())
                                .flatMap(mbi ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class))
                );
    }

    /**
     * update member's icon
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateIcon(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        memberBasicService.updateMemberBasicIcon(tuple2.getT1().getId(), tuple2.getT2()))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update member's qrCode
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateQrCode(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        memberBasicService.updateMemberBasicIcon(tuple2.getT1().getId(), tuple2.getT2()))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update member's profile
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateProfile(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(StringDataParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        memberBasicService.updateMemberBasicProfile(tuple2.getT1().getId(), tuple2.getT2()))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

}
