package com.blue.basic.component.encoder.ioc;

import com.blue.basic.component.encoder.api.conf.EncoderConf;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.component.encoder.api.common.StringColumnEncoder.init;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * initializer for string column encoder
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@ConditionalOnBean(value = {EncoderConf.class})
@Component
@Order(HIGHEST_PRECEDENCE)
public class StringColumnEncoderInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(StringColumnEncoderInitializer.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        List<EncoderConf> confList = new ArrayList<>(applicationContext.getBeansOfType(EncoderConf.class).values());
        if (confList.size() > 1)
            throw new RuntimeException("encoderConf bean can't be more than 1");

        EncoderConf encoderConf = confList.get(0);
        if (encoderConf == null)
            throw new RuntimeException("encoderConf bean can't be null");

        String salt = encoderConf.getSalt();
        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        init(salt);

        LOGGER.info("StringColumnEncoder init...");
    }

}