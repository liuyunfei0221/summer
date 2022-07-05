package com.blue.media.service.inter;

import com.blue.base.model.common.Access;
import com.blue.media.model.QrCodeGenerateParam;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * qr code generate service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface QrCodeGenerateService {

    /**
     * generate qr code
     *
     * @param access
     * @param qrCodeGenerateParam
     * @return
     */
    Mono<ServerResponse> generateCode(Access access, QrCodeGenerateParam qrCodeGenerateParam);

}