package com.blue.base.component.refresher.ioc;

import com.blue.base.common.message.ElementProcessor;
import com.blue.base.common.message.MessageProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

/**
 * static resources loader
 *
 * @author liuyunfei
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class StaticCommonRefresher implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        MessageProcessor.refresh();
        ElementProcessor.refresh();
    }

}
