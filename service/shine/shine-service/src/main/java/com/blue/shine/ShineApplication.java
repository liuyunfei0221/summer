package com.blue.shine;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.shine.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.shine.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.shine.remote")
public class ShineApplication {

    public static void main(String[] args) {
        run(ShineApplication.class, args);
    }

}
