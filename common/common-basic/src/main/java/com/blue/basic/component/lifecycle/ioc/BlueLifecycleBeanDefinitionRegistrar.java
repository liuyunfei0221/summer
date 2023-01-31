package com.blue.basic.component.lifecycle.ioc;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.component.common.BlueBeanDefinitionScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
import reactor.util.Logger;

import java.util.stream.Stream;

import static com.blue.basic.component.lifecycle.constant.BlueLifecycleScanConf.BLUE_LIFECYCLE_SCAN_PACKAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;
import static reactor.util.Loggers.getLogger;

/**
 * Registrar for BlueLifecycle obj
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaCommentsMustBeJavadocFormat"})
@Order(HIGHEST_PRECEDENCE)
public class BlueLifecycleBeanDefinitionRegistrar implements ResourceLoaderAware, ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = getLogger(BlueLifecycleBeanDefinitionRegistrar.class);

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = fromMap(importingClassMetadata.getAnnotationAttributes(EnableBlueLifecycle.class.getName()));
        String[] basePackages = ofNullable(mapperScanAttrs).map(attr -> attr.getStringArray(BLUE_LIFECYCLE_SCAN_PACKAGE.scanPackagesAttrName))
                .orElse(BLUE_LIFECYCLE_SCAN_PACKAGE.defaultScanPackages);

        LOGGER.info("basePackages = {}", Stream.of(basePackages).collect(toList()));

        BlueBeanDefinitionScanner scanner = new BlueBeanDefinitionScanner(registry, BLUE_LIFECYCLE_SCAN_PACKAGE.useDefaultFilters);

        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(BLUE_LIFECYCLE_SCAN_PACKAGE.targetType));
        scanner.doScan(basePackages);

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

}