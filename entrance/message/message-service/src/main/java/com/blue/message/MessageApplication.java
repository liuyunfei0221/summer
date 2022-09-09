package com.blue.message;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * message application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.message.event")
@DubboComponentScan(basePackages = "com.blue.message.remote")
public class MessageApplication {

    public static void main(String[] args) {
        run(MessageApplication.class, args);
    }

}
