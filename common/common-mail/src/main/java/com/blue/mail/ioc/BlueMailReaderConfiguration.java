package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailReaderConf;
import com.blue.mail.component.MailReader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.mail.api.generator.BlueMailReaderGenerator.generateMailReader;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {MailReaderConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueMailReaderConfiguration {

    @Bean
    MailReader mailReader(MailReaderConf mailReaderConf) {
        return generateMailReader(mailReaderConf);
    }

}
