package com.blue.base.anno;

import com.blue.base.component.lifecycle.ioc.BlueLifecycleBeanDefinitionRegistrar;
import com.blue.base.component.lifecycle.ioc.BlueSmartLifecycleController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to configure the start and stop sequence of certain beans
 *
 * @author DarkBlue
 */
@Target(TYPE)
@Retention(RUNTIME)
@Configuration
@Import({BlueLifecycleBeanDefinitionRegistrar.class, BlueSmartLifecycleController.class})
public @interface EnableBlueLifecycle {

    String[] basePackages() default {};

}
