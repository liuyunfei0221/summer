package com.blue.basic.component.message.ioc;

import com.blue.basic.component.message.api.conf.MessageConf;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import reactor.util.Logger;

import static com.blue.basic.component.message.api.loader.MessageLoader.load;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.util.Loggers.getLogger;

/**
 * messages loader configuration
 *
 * @author liuyunfei
 */
@Order(HIGHEST_PRECEDENCE)
public class BlueMessageLoadConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(BlueMessageLoadConfiguration.class);

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        MessageConf messageConf;
        try {
            messageConf = applicationContext.getBean(MessageConf.class);
            LOGGER.info("messageConf = {}", messageConf);
        } catch (BeansException e) {
            LOGGER.error("getBean(MessageConf.class), e = {}", e);
            throw new RuntimeException("getBean(MessageConf.class) failed");
        }

        load(messageConf);
    }

}