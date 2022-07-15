package com.blue.analyze;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * analyze application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.analyze.event")
@EnableBlueDataAccess(basePackages = "com.blue.analyze.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.analyze.remote")
public class AnalyzeApplication {

    public static void main(String[] args) {
        run(AnalyzeApplication.class, args);
    }

}
