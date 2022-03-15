package com.blue.verify.config.blue;

import com.blue.mail.api.conf.MailSenderConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mail sender config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "sender")
public class BlueMailSenderConfig extends MailSenderConfParams {
}
