package com.blue.marketing;

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
@EnableBlueLifecycle(basePackages = "com.blue.marketing.event")
@EnableBlueDataAccess(basePackages = "com.blue.marketing.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.marketing.remote")
public class MarketingApplication {

    public static void main(String[] args) {
        run(MarketingApplication.class, args);
    }

}
