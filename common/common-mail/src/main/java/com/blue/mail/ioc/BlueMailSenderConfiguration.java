package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailConf;
import com.blue.mail.common.MailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.mail.api.generator.BlueMailSenderGenerator.generateMailSender;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {MailConf.class})
@Configuration
public class BlueMailSenderConfiguration {

    @Bean
    MailSender mailSender(MailConf mailConf) {
        return generateMailSender(mailConf);
    }

}
