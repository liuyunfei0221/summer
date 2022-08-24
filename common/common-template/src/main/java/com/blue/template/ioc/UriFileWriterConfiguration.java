package com.blue.template.ioc;

import com.blue.template.api.conf.UriFileWriterConf;
import com.blue.template.component.UriFileTemplateWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.template.api.generator.TemplateWriterGenerator.generateUriFileTemplateWriter;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * package file writer configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {UriFileWriterConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class UriFileWriterConfiguration {

    @Bean
    UriFileTemplateWriter uriFileTemplateWriter(UriFileWriterConf uriFileWriterConf) {
        return generateUriFileTemplateWriter(uriFileWriterConf);
    }

}