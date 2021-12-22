package com.blue.member.config.universal;

import com.blue.qr.api.conf.QrConfParams;
import com.blue.qr.common.QrCoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.qr.api.generator.BlueQrCoderGenerator.generateQrCoder;


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
        return generateQrCoder(new QrConfParams());
    }

}
