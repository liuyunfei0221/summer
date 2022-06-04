package com.blue.event;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * event application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.event.event")
@EnableBlueDataAccess(basePackages = "com.blue.event.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.event.remote")
public class EventApplication {

    public static void main(String[] args) {
        run(EventApplication.class, args);
    }

}
