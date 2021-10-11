package com.blue.member.config.universal;

import com.blue.qr.api.conf.QrConfParams;
import com.blue.qr.api.generator.BlueQrCoderGenerator;
import com.blue.qr.common.QrCoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * qrcode config
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Configuration
public class QrCodeConfig {

    @Bean
    QrCoder qrCoder() {
        return BlueQrCoderGenerator.createQrCoder(new QrConfParams());
    }

}
