package com.blue.media.component.file.ioc;

import com.blue.base.component.common.BlueBeanDefinitionScanner;
import com.blue.media.component.file.inter.ByteHandler;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * byte handler registrar
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@Import(ByteHandlerBeanDefinitionRegistrar.class)
@Configuration
@Order(HIGHEST_PRECEDENCE)
public class ByteHandlerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private final ResourceLoader resourceLoader;

    public ByteHandlerBeanDefinitionRegistrar(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private static final String[] SCAN_PACKAGES = new String[]{"com.blue.media.component.file.impl"};
    private static final boolean USE_DEFAULT_FILTERS = false;
    private static final Class<?> TARGET_TYPE = ByteHandler.class;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        BlueBeanDefinitionScanner scanner = new BlueBeanDefinitionScanner(registry, USE_DEFAULT_FILTERS);

        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(TARGET_TYPE));
        scanner.doScan(SCAN_PACKAGES);

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

}
