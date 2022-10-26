package com.blue.agreement;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static org.springframework.boot.SpringApplication.run;

/**
 * agreement application
 *
 * @author liuyunfei
 */
//@SummerSpringBootApplication
@SpringBootApplication
@EnableDiscoveryClient
//@EnableBlueLifecycle(basePackages = "com.blue.agreement.event")
//@DubboComponentScan(basePackages = "com.blue.agreement.remote")

@EnableR2dbcRepositories(basePackages = {"com.blue.agreement.repository.template"})
//@EnableTransactionManagement
//@EnableR2dbcAuditing
public class AgreementApplication {

    public static void main(String[] args) {
        run(AgreementApplication.class, args);
    }

}
