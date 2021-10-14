package com.blue.secure;

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
@EnableBlueLifecycle(basePackages = "com.blue.secure.event")
@EnableBlueDataAccess(basePackages = "com.blue.secure.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.secure.remote")
public class SecureApplication {

    public static void main(String[] args) {
        run(SecureApplication.class, args);
    }

}
