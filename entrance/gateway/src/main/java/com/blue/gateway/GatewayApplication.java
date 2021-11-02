package com.blue.gateway;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
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
@DubboComponentScan(basePackages = "com.blue.gateway.remote")
public class GatewayApplication {

    public static void main(String[] args) {
        run(GatewayApplication.class, args);
    }

}