package com.blue.analyze.config.universal;

import com.blue.analyze.component.statistics.StatisticsProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * statistics starter
 *
 * @author liuyunfei
 */
@Configuration
@Import(StatisticsProcessor.class)
public class StatisticsConfig {
}
