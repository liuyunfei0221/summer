package com.blue.analyze.config.universal;

import com.blue.analyze.component.statistics.StatisticsProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * statistics starter
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@Configuration
@Import(StatisticsProcessor.class)
public class StatisticsConfig {
}
