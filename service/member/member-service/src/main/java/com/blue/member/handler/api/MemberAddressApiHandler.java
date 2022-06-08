package com.blue.member.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.model.MemberAddressInsertParam;
import com.blue.member.model.MemberAddressUpdateParam;
import com.blue.member.service.inter.MemberAddressService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.PathVariable.ID;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * member address api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberAddressApiHandler {

    private final MemberAddressService memberAddressService;

    public MemberAddressApiHandler(MemberAddressService memberAddressService) {
        this.memberAddressService = memberAddressService;
    }

    /**
     * create a new address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberAddressInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> memberAddressService.insertMemberAddress(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mai, serverRequest), BlueResponse.class));
    }

    /**
     * update address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberAddressUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> memberAddressService.updateMemberAddress(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mai, serverRequest), BlueResponse.class));
    }

    /**
     * delete address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key), getAccessReact(serverRequest))
                .flatMap(tuple2 -> memberAddressService.deleteMemberAddress(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mai, serverRequest), BlueResponse.class));
    }

    /**
     * select address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc -> memberAddressService.selectMemberAddressInfoMonoByMemberId(acc.getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mai, serverRequest), BlueResponse.class));
    }

}