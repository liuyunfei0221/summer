package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.component.MailSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.mail.api.generator.BlueMailSenderGenerator.generateMailSender;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {MailSenderConf.class})
@AutoConfiguration
public class BlueMailSenderConfiguration {

    @Bean
    MailSender mailSender(MailSenderConf mailSenderConf) {
        return generateMailSender(mailSenderConf);
    }

}
