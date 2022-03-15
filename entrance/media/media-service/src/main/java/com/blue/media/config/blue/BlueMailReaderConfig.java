package com.blue.media.config.blue;

import com.blue.mail.api.conf.MailReaderConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mail reader config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "reader")
public class BlueMailReaderConfig extends MailReaderConfParams {
}
