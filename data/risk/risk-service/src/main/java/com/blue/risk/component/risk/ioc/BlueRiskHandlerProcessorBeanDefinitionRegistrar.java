package com.blue.risk.component.risk.ioc;

import com.blue.base.component.common.BlueBeanDefinitionScanner;
import com.blue.risk.component.risk.inter.RiskHandler;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
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
 * risk handler registrar
 *
 * @author liuyunfei
 */
@Import(BlueRiskHandlerProcessorBeanDefinitionRegistrar.class)
@Configuration
@Order(HIGHEST_PRECEDENCE)
public class BlueRiskHandlerProcessorBeanDefinitionRegistrar implements ResourceLoaderAware, ImportBeanDefinitionRegistrar {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private static final String[] SCAN_PACKAGES = new String[]{"com.blue.risk.component.risk.impl"};
    private static final boolean USE_DEFAULT_FILTERS = false;
    private static final Class<?> TARGET_TYPE = RiskHandler.class;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        BlueBeanDefinitionScanner scanner = new BlueBeanDefinitionScanner(registry, USE_DEFAULT_FILTERS);

        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(TARGET_TYPE));
        scanner.doScan(SCAN_PACKAGES);

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

}
