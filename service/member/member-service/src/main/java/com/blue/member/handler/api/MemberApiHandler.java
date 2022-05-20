package com.blue.member.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.member.service.inter.MemberBasicService;
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
 * member api handler
 *
 * @author liuyunfei
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
    public Mono<ServerResponse> getMemberInfo(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        memberBasicService.getMemberBasicInfoMonoWithAssert(ai.getId())
                                .flatMap(mbi ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class))
                );
    }

}
