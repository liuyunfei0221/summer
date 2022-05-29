package com.blue.article;

import com.blue.base.anno.EnableBlueLifecycle;
import com.blue.base.anno.SummerSpringBootApplication;
import com.blue.database.anno.EnableBlueDataAccess;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author liuyunfei
 */
@SuppressWarnings("resource")
@SummerSpringBootApplication
@EnableDiscoveryClient
@EnableBlueLifecycle(basePackages = "com.blue.article.config.mq")
@EnableBlueDataAccess(basePackages = "com.blue.article.repository.mapper")
@DubboComponentScan(basePackages = "com.blue.article.remote")
public class ArticleApplication {

    public static void main(String[] args) {
        run(ArticleApplication.class, args);
    }

}
