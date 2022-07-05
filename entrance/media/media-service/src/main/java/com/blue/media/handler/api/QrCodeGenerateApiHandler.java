package com.blue.media.handler.api;

import com.blue.base.model.exps.BlueException;
import com.blue.media.model.QrCodeGenerateParam;
import com.blue.media.service.inter.QrCodeGenerateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static reactor.core.publisher.Mono.*;

/**
 * generate qr code api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public class QrCodeGenerateApiHandler {

    private final QrCodeGenerateService qrCodeGenerateService;

    public QrCodeGenerateApiHandler(QrCodeGenerateService qrCodeGenerateService) {
        this.qrCodeGenerateService = qrCodeGenerateService;
    }

    /**
     * generate qr code
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return zip(getAccessReact(serverRequest),
                serverRequest.bodyToMono(QrCodeGenerateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                .flatMap(tuple2 ->
                        qrCodeGenerateService.generateCode(tuple2.getT1(), tuple2.getT2()));
    }

}
