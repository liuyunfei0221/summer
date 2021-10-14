package com.blue.data;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import com.blue.dubbo.anno.EnableBlueDubbo;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.data.event")
@EnableBlueDataAccess(basePackages = "com.blue.data.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.data.remote")
public class DataApplication {

    public static void main(String[] args) {
        run(DataApplication.class, args);
    }

}
