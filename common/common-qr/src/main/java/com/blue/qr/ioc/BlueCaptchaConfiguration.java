package com.blue.qr.ioc;

import com.blue.qr.api.conf.QrConf;
import com.blue.qr.component.QrCoder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.qr.api.generator.BlueQrCoderGenerator.generateQrCoder;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {QrConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueCaptchaConfiguration {

    @Bean
    QrCoder qrCoder(QrConf qrConf) {
        return generateQrCoder(qrConf);
    }

}