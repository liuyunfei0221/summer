package com.blue.media;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * media application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.media.event")
@EnableBlueDataAccess(basePackages = "com.blue.media.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.media.remote")
public class MediaApplication {

    public static void main(String[] args) {
        run(MediaApplication.class, args);
    }

}