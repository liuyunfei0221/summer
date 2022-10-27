package com.blue.agreement;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * agreement application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.agreement.event")
@EnableBlueDataAccess(basePackages = "com.blue.agreement.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.agreement.remote")
public class AgreementApplication {

    public static void main(String[] args) {
        run(AgreementApplication.class, args);
    }

}
