package com.blue.member;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;


/**
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.member.config.mq")
@EnableBlueDataAccess(typeHandlerPackages = "com.blue.member.repository.type", basePackages = "com.blue.member.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.member.remote")
public class MemberApplication {

    public static void main(String[] args) {
        run(MemberApplication.class, args);
    }

}
