package com.blue.file;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * file application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.file.event")
@EnableBlueDataAccess(basePackages = "com.blue.file.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.file.remote")
public class FileApplication {

    public static void main(String[] args) {
        run(FileApplication.class, args);
    }

}
