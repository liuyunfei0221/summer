package com.blue.business;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.dubbo.anno.EnableBlueDubbo;
import com.blue.database.anno.EnableBlueDataAccess;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.business.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.business.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.business.remote")
public class BusinessApplication {

    public static void main(String[] args) {
        run(BusinessApplication.class, args);
    }

}
