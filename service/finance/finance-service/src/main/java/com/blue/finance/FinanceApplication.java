package com.blue.finance;

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
@EnableBlueLifecycle(basePackages = "com.blue.finance.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.finance.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.finance.remote")
public class FinanceApplication {

    public static void main(String[] args) {
        run(FinanceApplication.class, args);
    }

}
