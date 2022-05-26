package com.blue.qr.ioc;

import com.blue.qr.api.conf.QrConf;
import com.blue.qr.component.QrCoder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.qr.api.generator.BlueQrCoderGenerator.generateQrCoder;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {QrConf.class})
@AutoConfiguration
public class BlueCaptchaConfiguration {

    @Bean
    QrCoder qrCoder(QrConf qrConf) {
        return generateQrCoder(qrConf);
    }

}