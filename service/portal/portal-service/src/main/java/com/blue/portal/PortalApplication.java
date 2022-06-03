package com.blue.portal;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * portal application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.portal.event")
@EnableBlueDataAccess(basePackages = "com.blue.portal.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.portal.remote")
public class PortalApplication {

    public static void main(String[] args) {
        run(PortalApplication.class, args);
    }

}
