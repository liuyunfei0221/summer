package com.blue.auth;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author liuyunfei
 */
@SuppressWarnings("resource")
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.auth.event")
@EnableBlueDataAccess(basePackages = "com.blue.auth.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.auth.remote")
public class AuthApplication {

    public static void main(String[] args) {
        run(AuthApplication.class, args);
    }

}
