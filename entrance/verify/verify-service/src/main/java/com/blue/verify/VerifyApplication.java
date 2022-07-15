package com.blue.verify;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * verify application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.verify.event")
@DubboComponentScan(basePackages = "com.blue.verify.remote")
public class VerifyApplication {

    public static void main(String[] args) {
        run(VerifyApplication.class, args);
    }

}
