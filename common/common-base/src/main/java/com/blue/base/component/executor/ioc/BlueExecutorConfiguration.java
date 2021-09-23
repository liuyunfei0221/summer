package com.blue.base.component.executor.ioc;

import com.blue.base.component.executor.api.conf.ExecutorConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

import static com.blue.base.component.executor.api.generator.BlueExecutorGenerator.createExecutorService;

/**
 * 公共线程池配置类
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = {ExecutorConf.class})
@Configuration
public class BlueExecutorConfiguration {

    @Bean
    ExecutorService executorService(ExecutorConf executorConf) {
        return createExecutorService(executorConf);
    }

}
