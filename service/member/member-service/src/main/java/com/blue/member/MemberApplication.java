package com.blue.member;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;


/**
 * member application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.member.event")
@EnableBlueDataAccess(basePackages = "com.blue.member.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.member.remote")
public class MemberApplication {

    public static void main(String[] args) {
        run(MemberApplication.class, args);
    }

}
