package com.blue.template.ioc;

import com.blue.template.api.conf.PackageFileWriterConf;
import com.blue.template.component.PackageFileTemplateWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.template.api.generator.TemplateWriterGenerator.generatePackageFileTemplateWriter;

/**
 * package file writer configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {PackageFileWriterConf.class})
@AutoConfiguration
public class PackageFileWriterConfiguration {

    @Bean
    PackageFileTemplateWriter packageFileTemplateWriter(PackageFileWriterConf packageFileWriterConf) {
        return generatePackageFileTemplateWriter(packageFileWriterConf);
    }

}