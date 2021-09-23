package com.blue.data.config.blue;

import com.blue.base.component.executor.api.conf.ExecutorConfParam;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.RejectedExecutionHandler;

import static reactor.util.Loggers.getLogger;

/**
 * 通用线程池配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "executor")
public class BlueExecutorConfig extends ExecutorConfParam {

    private static final Logger LOGGER = getLogger(BlueExecutorConfig.class);

    @Override
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return (r, executor) -> {
            LOGGER.warn("触发线程池拒绝策略,交由调用线程执行");
            r.run();
        };
    }

}
