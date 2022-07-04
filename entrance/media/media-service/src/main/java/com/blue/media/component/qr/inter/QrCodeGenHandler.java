package com.blue.media.component.qr.inter;

import com.blue.base.constant.media.QrCodeGenType;
import com.blue.base.model.common.Access;
import com.blue.media.api.model.QrCodeConfigInfo;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * qr code generate handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface QrCodeGenHandler {

    /**
     * generate
     *
     * @param access
     * @param param
     * @param qrCodeConfigInfo
     * @return
     */
    Mono<byte[]> generate(Access access, Map<String, String> param, QrCodeConfigInfo qrCodeConfigInfo);

    /**
     * target qr code gen type to process
     *
     * @return
     */
    QrCodeGenType targetType();

}
