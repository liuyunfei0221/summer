package com.blue.base.component.refresher.ioc;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import static com.blue.base.component.refresher.api.StaticCommonRefresher.refresh;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * static resource loader configuration
 *
 * @author liuyunfei
 */
@Order(HIGHEST_PRECEDENCE)
public class BlueRefresherConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        refresh();
    }

}
