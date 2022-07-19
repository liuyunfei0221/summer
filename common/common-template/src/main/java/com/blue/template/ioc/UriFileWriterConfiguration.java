package com.blue.template.ioc;

import com.blue.template.api.conf.UriFileWriterConf;
import com.blue.template.component.UriFileTemplateWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.template.api.generator.TemplateWriterGenerator.generateUriFileTemplateWriter;

/**
 * package file writer configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {UriFileWriterConf.class})
@AutoConfiguration
public class UriFileWriterConfiguration {

    @Bean
    UriFileTemplateWriter uriFileTemplateWriter(UriFileWriterConf uriFileWriterConf) {
        return generateUriFileTemplateWriter(uriFileWriterConf);
    }

}