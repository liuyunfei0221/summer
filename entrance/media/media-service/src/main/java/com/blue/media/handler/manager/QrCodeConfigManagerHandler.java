package com.blue.media.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.model.QrCodeConfigInsertParam;
import com.blue.media.model.QrCodeConfigUpdateParam;
import com.blue.media.service.inter.QrCodeConfigService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.media.constant.MediaTypeReference.PAGE_MODEL_FOR_QR_CODE_CONFIG_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * config manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class QrCodeConfigManagerHandler {

    private final QrCodeConfigService qrCodeConfigService;

    public QrCodeConfigManagerHandler(QrCodeConfigService qrCodeConfigService) {
        this.qrCodeConfigService = qrCodeConfigService;
    }

    /**
     * create a new config
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(QrCodeConfigInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> qrCodeConfigService.insertQrCodeConfig(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci), BlueResponse.class));
    }

    /**
     * update config
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(QrCodeConfigUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> qrCodeConfigService.updateQrCodeConfig(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci), BlueResponse.class));
    }

    /**
     * delete config
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(qrCodeConfigService::deleteQrCodeConfig)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci), BlueResponse.class));
    }

    /**
     * select config
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_QR_CODE_CONFIG_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(qrCodeConfigService::selectQrCodeConfigManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr), BlueResponse.class));
    }

}
