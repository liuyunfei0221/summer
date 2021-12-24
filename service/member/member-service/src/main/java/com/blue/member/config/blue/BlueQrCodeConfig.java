package com.blue.member.config.blue;

import com.blue.qr.api.conf.QrConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sync rest config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "qr")
public class BlueQrCodeConfig extends QrConfParams {
}
