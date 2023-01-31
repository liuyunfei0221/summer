package com.blue.basic.component.executor.ioc;

import com.blue.basic.component.executor.api.conf.ExecutorConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.concurrent.ExecutorService;

import static com.blue.basic.component.executor.api.generator.BlueExecutorGenerator.generateExecutorService;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * global executor configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {ExecutorConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueExecutorConfiguration {

    @Bean
    ExecutorService executorService(ExecutorConf executorConf) {
        return generateExecutorService(executorConf);
    }

}