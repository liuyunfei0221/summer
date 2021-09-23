package com.blue.dubbo.anno;

import com.blue.dubbo.ioc.BlueDubboComponentScanRegistrar;
import com.blue.dubbo.ioc.BlueDubboConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * conf data component
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
@Target(TYPE)
@Retention(RUNTIME)
@Configuration
@Import({BlueDubboConfiguration.class, BlueDubboComponentScanRegistrar.class})
public @interface EnableBlueDubbo {

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

}
