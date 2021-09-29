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
 * enable dubbo
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc"})
@Target(TYPE)
@Retention(RUNTIME)
@Configuration
@Import({BlueDubboConfiguration.class, BlueDubboComponentScanRegistrar.class})
public @interface EnableBlueDubbo {

    /**
     * scan packages
     *
     * @return
     */
    String[] basePackages() default {};

    /**
     * scan classes
     *
     * @return
     */
    Class<?>[] basePackageClasses() default {};

}
