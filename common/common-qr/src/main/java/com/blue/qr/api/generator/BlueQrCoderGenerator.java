package com.blue.qr.api.generator;

import com.blue.qr.api.conf.QrConfParams;
import com.blue.qr.api.conf.QrConf;
import com.blue.qr.component.QrCoder;

import static com.blue.base.common.base.BlueChecker.isNotNull;

/**
 * qrcoder generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SpellCheckingInspection"})
public final class BlueQrCoderGenerator {

    private static final QrConf DEFAULT_CONF = new QrConfParams();

    /**
     * generate qrcoder
     *
     * @return
     */
    public static QrCoder generateQrCoder() {
        return generateQrCoder(DEFAULT_CONF);
    }

    /**
     * generate qrcoder
     *
     * @param qrConf
     * @return
     */
    public static QrCoder generateQrCoder(QrConf qrConf) {
        return new QrCoder(isNotNull(qrConf) ? qrConf : DEFAULT_CONF);
    }

}
