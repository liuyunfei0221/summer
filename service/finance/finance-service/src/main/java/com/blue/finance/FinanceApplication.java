package com.blue.finance;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * finance application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.finance.event")
@EnableBlueDataAccess(basePackages = "com.blue.finance.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.finance.remote")
public class FinanceApplication {

    public static void main(String[] args) {
        run(FinanceApplication.class, args);
    }

}
