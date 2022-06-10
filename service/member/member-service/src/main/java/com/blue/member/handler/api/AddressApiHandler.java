package com.blue.member.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.model.AddressInsertParam;
import com.blue.member.model.AddressUpdateParam;
import com.blue.member.service.inter.AddressService;
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
 * address api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AddressApiHandler {

    private final AddressService addressService;

    public AddressApiHandler(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * create a new address
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(AddressInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> addressService.insertAddress(tuple2.getT1(), tuple2.getT2().getId()))
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
        return zip(serverRequest.bodyToMono(AddressUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> addressService.updateAddress(tuple2.getT1(), tuple2.getT2().getId()))
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
                .flatMap(tuple2 -> addressService.deleteAddress(tuple2.getT1(), tuple2.getT2().getId()))
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
                .flatMap(acc -> addressService.selectAddressInfoMonoByMemberId(acc.getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mai, serverRequest), BlueResponse.class));
    }

}