package com.blue.context.ioc;

import com.blue.context.component.ContextProcessFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * reactive context configuration
 *
 * @author liuyunfei
 */
@ConditionalOnWebApplication(type = REACTIVE)
@Configuration
public class BlueContextConfiguration {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    ContextProcessFilter contextHolderFilter() {
        return new ContextProcessFilter();
    }

}
