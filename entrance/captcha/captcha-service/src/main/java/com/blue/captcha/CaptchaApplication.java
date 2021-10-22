package com.blue.captcha;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import com.blue.dubbo.anno.EnableBlueDubbo;

import static org.springframework.boot.SpringApplication.run;

/**
 * captcha application
 *
 * @author DarkBlue
 */
@SummerSpringBootApplication
@EnableBlueLifecycle(basePackages = "com.blue.captcha.event")
@EnableBlueDataAccess(basePackages = "com.blue.captcha.repository.mapper")
@EnableBlueDubbo(basePackages = "com.blue.captcha.remote")
public class CaptchaApplication {

    public static void main(String[] args) {
        run(CaptchaApplication.class, args);
    }

}
