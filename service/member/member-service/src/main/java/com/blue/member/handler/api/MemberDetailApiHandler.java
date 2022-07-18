package com.blue.member.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.model.MemberDetailUpdateParam;
import com.blue.member.service.inter.MemberDetailService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
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
                        memberDetailService.getMemberDetailInfoMonoByMemberIdWithAssert(acc.getId())
                                .flatMap(mdi ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(success(mdi, serverRequest), BlueResponse.class))
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
                                .body(success(mbi, serverRequest), BlueResponse.class));
    }

}
