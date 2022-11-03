package com.blue.member.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.model.CardInsertParam;
import com.blue.member.model.CardUpdateParam;
import com.blue.member.service.inter.CardService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * card api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class CardApiHandler {

    private final CardService cardService;

    public CardApiHandler(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * create a new card
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CardInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> cardService.insertCard(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * update card
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CardUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> cardService.updateCard(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * delete card
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key), getAccessReact(serverRequest))
                .flatMap(tuple2 -> cardService.deleteCard(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * select card
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc -> cardService.selectCardInfoByMemberId(acc.getId()))
                .flatMap(mai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mai, serverRequest), BlueResponse.class));
    }

}