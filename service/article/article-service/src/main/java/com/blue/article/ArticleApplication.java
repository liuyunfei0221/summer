package com.blue.article;

import com.blue.basic.anno.EnableBlueLifecycle;
import com.blue.basic.anno.SummerSpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * article application
 *
 * @author liuyunfei
 */
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.article.event")
@DubboComponentScan(basePackages = "com.blue.article.remote")
public class ArticleApplication {

    public static void main(String[] args) {
        run(ArticleApplication.class, args);
    }

}
