package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailReaderConf;
import com.blue.mail.common.MailReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.mail.api.generator.BlueMailReaderGenerator.generateMailReader;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {MailReaderConf.class})
@Configuration
public class BlueMailReaderConfiguration {

    @Bean
    MailReader mailReader(MailReaderConf mailReaderConf) {
        return generateMailReader(mailReaderConf);
    }

}
