package com.blue.message;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * message application
 *
 * @author liuyunfei
 */
@SuppressWarnings("resource")
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.message.event")
@EnableBlueDataAccess(basePackages = "com.blue.message.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.message.remote")
public class MessageApplication {

    public static void main(String[] args) {
        run(MessageApplication.class, args);
    }

}
