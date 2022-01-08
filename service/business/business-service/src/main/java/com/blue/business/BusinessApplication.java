package com.blue.business;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import com.blue.database.anno.EnableBlueTransaction;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.business.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.business.repository.mapper")
@EnableBlueTransaction
@DubboComponentScan(basePackages = "com.blue.business.remote")
public class BusinessApplication {

    public static void main(String[] args) {
        run(BusinessApplication.class, args);
    }

}
