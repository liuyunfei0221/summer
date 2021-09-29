package com.blue.dubbo.ioc;

import com.blue.dubbo.anno.EnableBlueDubbo;
import org.apache.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationPostProcessor;
import org.apache.dubbo.config.spring.util.DubboBeanUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.blue.dubbo.constant.BlueDubboScanConf.BLUE_DUBBO_SCAN_PACKAGE;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.beans.factory.support.BeanDefinitionReaderUtils.registerWithGeneratedName;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;
import static org.springframework.util.ClassUtils.getPackageName;

/**
 * dubbo component scan registrar
 *
 * @author liuyunfei
 * @date 2021/8/25
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming", "AliControlFlowStatementWithoutBraces"})
public class BlueDubboComponentScanRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String SCAN_PACKAGES_ATTR_NAME = BLUE_DUBBO_SCAN_PACKAGE.scanPackagesAttrName;
    private static final String SCAN_CLASSES_ATTR_NAME = BLUE_DUBBO_SCAN_PACKAGE.scanClassesAttrName;
    private static final String[] DEFAULT_SCAN_PACKAGES = BLUE_DUBBO_SCAN_PACKAGE.defaultScanPackages;
    private static final int ROLE = 2;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        DubboBeanUtils.registerCommonBeans(registry);
        Set<String> packagesToScan = this.getPackagesToScan(importingClassMetadata);
        this.registerServiceAnnotationPostProcessor(packagesToScan, registry);
    }

    private void registerServiceAnnotationPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = rootBeanDefinition(ServiceAnnotationPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(ROLE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        registerWithGeneratedName(beanDefinition, registry);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = fromMap(metadata.getAnnotationAttributes(EnableBlueDubbo.class.getName()));

        String[] basePackages = ofNullable(attributes).map(attr -> attr.getStringArray(SCAN_PACKAGES_ATTR_NAME)).orElse(DEFAULT_SCAN_PACKAGES);
        Class<?>[] basePackageClasses = ofNullable(attributes).map(attr -> attr.getClassArray(SCAN_CLASSES_ATTR_NAME)).orElse(new Class<?>[0]);

        List<String> packagesToScan = new LinkedList<>(asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses)
            packagesToScan.add(getPackageName(basePackageClass));

        //noinspection SimplifyStreamApiCallChains
        return of(packagesToScan.stream().collect(toSet()))
                .filter(l -> l.size() > 0).orElse(singleton(getPackageName(metadata.getClassName())));
    }
}