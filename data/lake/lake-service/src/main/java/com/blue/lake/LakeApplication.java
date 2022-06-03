package com.blue.lake;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * lake application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.lake.event")
@EnableBlueDataAccess(basePackages = "com.blue.lake.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.lake.remote")
public class LakeApplication {

    public static void main(String[] args) {
        run(LakeApplication.class, args);
    }

}
