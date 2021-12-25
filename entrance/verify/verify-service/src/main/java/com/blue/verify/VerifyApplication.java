package com.blue.verify;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * verify application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.verify.event")
@EnableBlueDataAccess(basePackages = "com.blue.verify.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.verify.remote")
public class VerifyApplication {

    public static void main(String[] args) {
        run(VerifyApplication.class, args);
    }

}
