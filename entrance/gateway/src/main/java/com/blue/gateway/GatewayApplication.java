package com.blue.gateway;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.dubbo.anno.EnableBlueDubbo;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * gateway application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.gateway.event")
@EnableBlueDubbo(basePackages = "com.blue.gateway.remote")
public class GatewayApplication {

    public static void main(String[] args) {
        run(GatewayApplication.class, args);
    }

}