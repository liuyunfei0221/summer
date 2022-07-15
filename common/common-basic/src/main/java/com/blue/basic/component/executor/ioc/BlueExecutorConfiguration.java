package com.blue.basic.component.executor.ioc;

import com.blue.basic.component.executor.api.conf.ExecutorConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;

import static com.blue.basic.component.executor.api.generator.BlueExecutorGenerator.generateExecutorService;

/**
 * global executor configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {ExecutorConf.class})
@AutoConfiguration
public class BlueExecutorConfiguration {

    @Bean
    ExecutorService executorService(ExecutorConf executorConf) {
        return generateExecutorService(executorConf);
    }

}
