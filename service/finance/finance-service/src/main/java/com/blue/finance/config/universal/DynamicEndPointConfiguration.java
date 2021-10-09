package com.blue.finance.config.universal;

import com.blue.finance.handler.dynamic.BlueDynamicHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * dynamic endpoint configuration
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@Configuration
@Import(BlueDynamicHandler.class)
public class DynamicEndPointConfiguration {
}
