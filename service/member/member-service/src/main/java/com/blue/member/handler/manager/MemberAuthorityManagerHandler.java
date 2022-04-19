package com.blue.member.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.member.service.inter.MemberAuthorityService;
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

/**
 * member authority manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class MemberAuthorityManagerHandler {

    private final MemberAuthorityService memberAuthorityService;

    public MemberAuthorityManagerHandler(MemberAuthorityService memberAuthorityService) {
        this.memberAuthorityService = memberAuthorityService;
    }

    /**
     * select member authorities
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(
                        error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(memberAuthorityService::selectMemberAuthorityPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
