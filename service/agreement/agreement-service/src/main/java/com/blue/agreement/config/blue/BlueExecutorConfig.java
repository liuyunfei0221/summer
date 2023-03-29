package com.blue.agreement.config.blue;

import com.blue.basic.component.executor.api.conf.ExecutorConfParam;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * executor config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "executor")
public class BlueExecutorConfig extends ExecutorConfParam {

    private static final Logger LOGGER = getLogger(BlueExecutorConfig.class);

    @Override
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return (r, executor) -> {
            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
            r.run();
        };
    }

}
