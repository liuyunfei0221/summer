package com.blue.media;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * media application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.media.event")
@DubboComponentScan(basePackages = "com.blue.media.remote")
public class MediaApplication {

    public static void main(String[] args) {
        run(MediaApplication.class, args);
    }

}
