package com.blue.media.service.impl;

import com.blue.media.service.inter.QrCodeGenerateService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * qr code generate service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Service
public class QrCodeGenerateServiceImpl implements QrCodeGenerateService {

    /**
     * generate qr code
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return null;
    }

}
