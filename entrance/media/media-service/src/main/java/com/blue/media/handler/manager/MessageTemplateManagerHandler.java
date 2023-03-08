package com.blue.media.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.model.MessageTemplateInsertParam;
import com.blue.media.model.MessageTemplateUpdateParam;
import com.blue.media.service.inter.MessageTemplateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.media.constant.MediaTypeReference.PAGE_MODEL_FOR_MESSAGE_TEMPLATE_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * message template manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MessageTemplateManagerHandler {

    private final MessageTemplateService messageTemplateService;

    public MessageTemplateManagerHandler(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    /**
     * create a new message template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MessageTemplateInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> messageTemplateService.insertMessageTemplate(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(mi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mi), BlueResponse.class));
    }

    /**
     * update message template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MessageTemplateUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> messageTemplateService.updateMessageTemplate(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(mi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mi), BlueResponse.class));
    }

    /**
     * delete message template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(messageTemplateService::deleteMessageTemplate)
                .flatMap(mi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mi), BlueResponse.class));
    }

    /**
     * select message template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_MESSAGE_TEMPLATE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(messageTemplateService::selectMessageTemplateManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr), BlueResponse.class));
    }

}
