package com.blue.file;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import com.blue.dubbo.anno.EnableBlueDubbo;

import static org.springframework.boot.SpringApplication.run;

/**
 * file application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.file.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.file.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.file.remote")
public class FileApplication {

    public static void main(String[] args) {
        run(FileApplication.class, args);
    }

}
