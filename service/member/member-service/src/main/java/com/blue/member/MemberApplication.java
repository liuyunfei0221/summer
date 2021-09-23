package com.blue.member;

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
@EnableBlueLifecycle(basePackages = "com.blue.member.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.member.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.member.remote")
public class MemberApplication {

    public static void main(String[] args) {
        run(MemberApplication.class, args);
    }

}
