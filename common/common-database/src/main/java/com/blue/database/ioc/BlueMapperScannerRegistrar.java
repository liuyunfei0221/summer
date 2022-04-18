package com.blue.database.ioc;

import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.logging.log4j.core.config.Order;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.instantiateClass;
import static org.springframework.beans.factory.support.AbstractBeanDefinition.SCOPE_DEFAULT;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;
import static org.springframework.util.StringUtils.hasText;


/**
 * mybatis mapper scanner registrar
 *
 * @author liuyunfei
 * @date 2021/8/15
 * @apiNote
 */
@SuppressWarnings({"NullableProblems", "AliControlFlowStatementWithoutBraces"})
@Order(HIGHEST_PRECEDENCE)
public class BlueMapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final int IDX = 0;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = fromMap(importingClassMetadata.getAnnotationAttributes(EnableBlueDataAccess.class.getName()));
        if (isNotNull(mapperScanAttrs))
            registerBeanDefinitions(mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata));
    }

    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {

        BeanDefinitionBuilder builder = genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass))
            builder.addPropertyValue("annotationClass", annotationClass);

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface))
            builder.addPropertyValue("markerInterface", markerInterface);

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass))
            builder.addPropertyValue("nameGenerator", instantiateClass(generatorClass));

        @SuppressWarnings("rawtypes")
        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass))
            builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);

        String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
        if (hasText(sqlSessionTemplateRef))
            builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));

        String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
        if (hasText(sqlSessionFactoryRef))
            builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));

        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(stream(annoAttrs.getStringArray("typeHandlerPackages")).filter(StringUtils::hasText).collect(toList()));
        basePackages.addAll(stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(toList()));
        basePackages.addAll(stream(annoAttrs.getClassArray("basePackageClasses")).filter(Objects::nonNull).map(ClassUtils::getPackageName).collect(toList()));

        String lazyInitialization = annoAttrs.getString("lazyInitialization");
        if (hasText(lazyInitialization))
            builder.addPropertyValue("lazyInitialization", lazyInitialization);

        String defaultScope = annoAttrs.getString("defaultScope");
        if (!SCOPE_DEFAULT.equals(defaultScope))
            builder.addPropertyValue("defaultScope", defaultScope);

        builder.addPropertyValue("basePackage", collectionToCommaDelimitedString(basePackages));

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + IDX;
    }

}
