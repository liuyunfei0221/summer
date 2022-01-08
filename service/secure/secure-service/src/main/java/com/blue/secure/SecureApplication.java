package com.blue.secure;

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
@EnableBlueLifecycle(basePackages = "com.blue.secure.event")
@EnableBlueDataAccess(basePackages = "com.blue.secure.repository.mapper")
@EnableBlueTransaction
@DubboComponentScan(basePackages = "com.blue.secure.remote")
public class SecureApplication {

    public static void main(String[] args) {
        run(SecureApplication.class, args);
    }

}
