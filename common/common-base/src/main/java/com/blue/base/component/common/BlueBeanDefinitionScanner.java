package com.blue.base.component.common;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * 用于定制化配置spring扫描的scanner
 *
 * @author DarkBlue
 * @date 2021/8/15
 * @apiNote
 */
@SuppressWarnings("unused")
public final class BlueBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public BlueBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public BlueBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public BlueBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public BlueBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    @Override
    @NonNull
    public Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        return super.doScan(basePackages);
    }

}
