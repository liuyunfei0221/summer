package com.blue.risk;

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
@EnableBlueLifecycle(basePackages = "com.blue.risk.event")
@EnableBlueDataAccess(basePackages = "com.blue.risk.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.risk.remote")
public class RiskApplication {

    public static void main(String[] args) {
        run(RiskApplication.class, args);
    }

}
