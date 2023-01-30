package com.blue.template.ioc;

import com.blue.template.api.conf.StringContentWriterConf;
import com.blue.template.component.StringContentTemplateWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.template.api.generator.TemplateWriterGenerator.generateStringContentTemplateWriter;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * string content writer configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {StringContentWriterConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class StringContentWriterConfiguration {

    @Bean
    StringContentTemplateWriter stringContentTemplateWriter(StringContentWriterConf stringContentWriterConf) {
        return generateStringContentTemplateWriter(stringContentWriterConf);
    }

}