package com.blue.mail.ioc;

import com.blue.mail.api.conf.MailReaderConf;
import com.blue.mail.component.MailReader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.mail.api.generator.BlueMailReaderGenerator.generateMailReader;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {MailReaderConf.class})
@AutoConfiguration
public class BlueMailReaderConfiguration {

    @Bean
    MailReader mailReader(MailReaderConf mailReaderConf) {
        return generateMailReader(mailReaderConf);
    }

}
