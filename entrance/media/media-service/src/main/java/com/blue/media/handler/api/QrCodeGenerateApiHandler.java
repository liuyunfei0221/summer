package com.blue.media.handler.api;

import com.blue.media.service.inter.QrCodeGenerateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        return qrCodeGenerateService.generate(serverRequest);
    }

}
