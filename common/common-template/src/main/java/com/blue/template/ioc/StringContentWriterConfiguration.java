package com.blue.template.ioc;

import com.blue.template.api.conf.StringContentWriterConf;
import com.blue.template.component.StringContentTemplateWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.template.api.generator.TemplateWriterGenerator.generateStringContentTemplateWriter;

/**
 * string content writer configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {StringContentWriterConf.class})
@AutoConfiguration
public class StringContentWriterConfiguration {

    @Bean
    StringContentTemplateWriter stringContentTemplateWriter(StringContentWriterConf stringContentWriterConf) {
        return generateStringContentTemplateWriter(stringContentWriterConf);
    }

}