package com.blue.member.handler.manager;

import com.blue.base.model.base.BlueResult;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/**
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public class MemberManagerHandler {

    private final MemberBasicService memberBasicService;

    public MemberManagerHandler(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * 测试查询成员
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {

        return memberBasicService.selectMember()
                .flatMap(ml -> {
                    for (MemberBasic mb : ml) {
                        mb.setPassword("");
                    }
                    return just(ml);
                }).flatMap(ml ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ml, OK.message), BlueResult.class)
                );
    }

}
