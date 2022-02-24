package com.blue.qr.ioc;

import com.blue.qr.api.conf.QrConf;
import com.blue.qr.common.QrCoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.qr.api.generator.BlueQrCoderGenerator.generateQrCoder;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@ConditionalOnBean(value = {QrConf.class})
@Configuration
public class BlueCaptchaConfiguration {

    @Bean
    QrCoder qrCoder(QrConf qrConf) {
        return generateQrCoder(qrConf);
    }

}