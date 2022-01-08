package com.blue.database.anno;

import com.blue.database.ioc.BlueTransactionConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * enable trans
 *
 * @author DarkBlue
 */
@Target(TYPE)
@Retention(RUNTIME)
@Configuration
@Import(BlueTransactionConfiguration.class)
public @interface EnableBlueTransaction {
}
