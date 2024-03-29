package com.blue.media.component.qr.inter;

import com.blue.basic.constant.media.QrCodeType;
import com.blue.basic.model.common.Access;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.model.QrCodeGenerateParam;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * qr code generate handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface QrCodeGenerateHandler {

    /**
     * generate
     *
     * @param qrCodeConfigInfo
     * @param qrCodeGenerateParam
     * @param access
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generateCode(QrCodeGenerateParam qrCodeGenerateParam, QrCodeConfigInfo qrCodeConfigInfo, Access access, ServerRequest serverRequest);

    /**
     * target qr code type to process
     *
     * @return
     */
    QrCodeType targetType();

}