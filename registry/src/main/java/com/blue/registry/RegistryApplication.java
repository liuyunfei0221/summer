package com.blue.registry;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import static org.springframework.boot.SpringApplication.run;

/**
 * registry applicaton
 *
 * @author liuyunfei
 */
@SuppressWarnings("resource")
@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication {

    public static void main(String[] args) {
        run(RegistryApplication.class, args);
    }

}
