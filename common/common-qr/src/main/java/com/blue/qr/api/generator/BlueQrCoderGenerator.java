package com.blue.qr.api.generator;

import com.blue.qr.api.conf.QrConfParams;
import com.blue.qr.api.conf.QrConf;
import com.blue.qr.common.QrCoder;

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
        return new QrCoder(qrConf != null ? qrConf : DEFAULT_CONF);
    }

}
