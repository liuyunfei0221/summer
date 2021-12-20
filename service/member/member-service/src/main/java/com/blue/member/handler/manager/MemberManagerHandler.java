package com.blue.member.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * member manager handler
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class MemberManagerHandler {

    private final MemberBasicService memberBasicService;

    public MemberManagerHandler(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * select members
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(
                        error(new BlueException(EMPTY_PARAM)))
                .flatMap(memberBasicService::selectMemberInfoPageMonoByPageAndCondition)
                .flatMap(vo ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, vo, OK.message), BlueResponse.class));
    }


}
