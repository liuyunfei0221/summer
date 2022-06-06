package com.blue.member.handler.manager;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.service.inter.MemberAddressService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.member.constant.MemberTypeReference.PAGE_MODEL_FOR_MEMBER_ADDRESS_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * member address manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class MemberAddressManagerHandler {

    private final MemberAddressService memberAddressService;

    public MemberAddressManagerHandler(MemberAddressService memberAddressService) {
        this.memberAddressService = memberAddressService;
    }

    /**
     * select member address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_MEMBER_ADDRESS_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(memberAddressService::selectMemberAddressInfoPageMonoByPageAndCondition)
                .flatMap(pma ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pma, serverRequest), BlueResponse.class));
    }


}
