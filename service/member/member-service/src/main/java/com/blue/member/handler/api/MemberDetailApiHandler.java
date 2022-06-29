package com.blue.member.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.common.StatusParam;
import com.blue.base.model.exps.BlueException;
import com.blue.member.model.MemberDetailUpdateParam;
import com.blue.member.service.inter.MemberDetailService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * member detail api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberDetailApiHandler {

    private final MemberDetailService memberDetailService;

    public MemberDetailApiHandler(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    /**
     * get member detail info by access
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        memberDetailService.getMemberDetailInfoMonoWithAssert(acc.getId())
                                .flatMap(mdi ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mdi, serverRequest), BlueResponse.class))
                );
    }

    /**
     * update member detail
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(MemberDetailUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        just(memberDetailService.updateMemberDetail(tuple2.getT1().getId(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update member detail status
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateStatus(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(StatusParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        just(memberDetailService.updateMemberDetailStatus(tuple2.getT1().getId(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

}
