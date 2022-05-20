package com.blue.base;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import com.blue.database.anno.EnableBlueTransaction;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author liuyunfei
 */
@SuppressWarnings("resource")
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.base.event")
@EnableBlueDataAccess(basePackages = "com.blue.base.repository.mapper")
@EnableBlueTransaction
@DubboComponentScan(basePackages = "com.blue.base.remote")
public class BaseApplication {

    public static void main(String[] args) {
        run(BaseApplication.class, args);
    }

}
