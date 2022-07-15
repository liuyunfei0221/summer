package com.blue.base;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * base application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.base.event")
@EnableBlueDataAccess(basePackages = "com.blue.base.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.base.remote")
public class BaseApplication {

    public static void main(String[] args) {
        run(BaseApplication.class, args);
    }

}
