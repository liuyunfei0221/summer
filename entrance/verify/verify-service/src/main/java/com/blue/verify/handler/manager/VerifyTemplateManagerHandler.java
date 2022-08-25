package com.blue.verify.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.verify.model.VerifyTemplateInsertParam;
import com.blue.verify.model.VerifyTemplateUpdateParam;
import com.blue.verify.service.inter.VerifyTemplateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.verify.constant.VerifyTypeReference.PAGE_MODEL_FOR_VERIFY_TEMPLATE_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * verify template manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class VerifyTemplateManagerHandler {

    private final VerifyTemplateService verifyTemplateService;

    public VerifyTemplateManagerHandler(VerifyTemplateService verifyTemplateService) {
        this.verifyTemplateService = verifyTemplateService;
    }

    /**
     * create a new verify template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(VerifyTemplateInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> verifyTemplateService.insertVerifyTemplate(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ti ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ti), BlueResponse.class));
    }

    /**
     * update verify template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(VerifyTemplateUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> verifyTemplateService.updateVerifyTemplate(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ti ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ti), BlueResponse.class));
    }

    /**
     * delete verify template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(verifyTemplateService::deleteVerifyTemplate)
                .flatMap(ti ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ti), BlueResponse.class));
    }

    /**
     * select verify template
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_VERIFY_TEMPLATE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(verifyTemplateService::selectVerifyTemplateManagerInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr), BlueResponse.class));
    }

}
