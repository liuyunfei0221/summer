package com.blue.shine.config.blue;

import com.blue.identity.api.conf.BaseIdentityConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static java.time.Instant.now;

/**
 * 主键生成配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "identity")
public class BlueIdentityConfig extends BaseIdentityConfParams {

    @Override
    public Long getLastSeconds() {
        return now().getEpochSecond();
    }

    @Override
    public Consumer<Long> getMaximumTimeAlarm() {
        return seconds -> System.err.println("Maximum time to reach " + seconds);
    }

    @Override
    public Consumer<Long> getSecondsRecorder() {
        return null;
    }

}
