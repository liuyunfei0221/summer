package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.component.MailSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.mail.api.generator.BlueMailSenderGenerator.generateMailSender;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {MailSenderConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueMailSenderConfiguration {

    @Bean
    MailSender mailSender(MailSenderConf mailSenderConf) {
        return generateMailSender(mailSenderConf);
    }

}
