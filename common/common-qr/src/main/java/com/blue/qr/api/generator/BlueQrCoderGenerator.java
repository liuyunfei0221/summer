package com.blue.qr.api.generator;

import com.blue.qr.api.conf.QrConfParams;
import com.blue.qr.api.conf.QrConf;
import com.blue.qr.common.QrCoder;

/**
 * redis组件创建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueQrCoderGenerator {

    private static final QrConf DEFAULT_CONF = new QrConfParams();

    /**
     * 创建二维码工具
     *
     * @return
     */
    public static QrCoder createQrCoder() {
        return new QrCoder(DEFAULT_CONF);
    }

    /**
     * 创建二维码工具
     *
     * @param qrConf
     * @return
     */
    public static QrCoder createQrCoder(QrConf qrConf) {
        return new QrCoder(qrConf != null ? qrConf : DEFAULT_CONF);
    }

}
