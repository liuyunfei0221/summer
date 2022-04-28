package com.blue.context.ioc;

import com.blue.context.component.ContextHolderFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

/**
 * reactive context configuration
 *
 * @author liuyunfei
 */
@ConditionalOnWebApplication(type = REACTIVE)
@Configuration
public class BlueContextConfiguration {

    @Bean
    ContextHolderFilter contextHolderFilter() {
        return new ContextHolderFilter();
    }

}
