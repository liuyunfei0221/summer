package com.blue.data.config.universal;

import com.blue.data.common.statistics.StatisticsProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@Configuration
@Import(StatisticsProcessor.class)
public class StatisticsConfig {
}
