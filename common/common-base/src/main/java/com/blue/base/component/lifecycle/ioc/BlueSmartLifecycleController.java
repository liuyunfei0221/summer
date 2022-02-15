package com.blue.base.component.lifecycle.ioc;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.exps.BlueException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;
import reactor.util.Logger;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Comparator.comparingInt;
import static reactor.util.Loggers.getLogger;

/**
 * controller for blue lifecycle
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public class BlueSmartLifecycleController implements ApplicationContextAware, SmartLifecycle {

    private static final Logger LOGGER = getLogger(BlueLifecycleBeanDefinitionRegistrar.class);

    private ApplicationContext applicationContext;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private Map<String, BlueLifecycle> beans;

    /**
     * order
     */
    private static final Comparator<Map.Entry<String, BlueLifecycle>>
            COMPARATOR_FOR_START = comparingInt(e -> e.getValue().startPrecedence()),
            COMPARATOR_FOR_STOP = comparingInt(e -> e.getValue().stopPrecedence());

    /**
     * action
     */
    private static final Consumer<Map.Entry<String, BlueLifecycle>>
            ACTION_FOR_START = entry -> {
        BlueLifecycle blueLifecycle = entry.getValue();
        try {
            blueLifecycle.start();
            LOGGER.info("start() success, {} started, precedence is {}", entry.getKey(), blueLifecycle.startPrecedence());
        } catch (Exception e) {
            String beanName = entry.getKey();
            int precedence = blueLifecycle.startPrecedence();
            LOGGER.error("start() failed, {} start failed, precedence is {}, e = {}", beanName, precedence, e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "stop() failed, " + beanName + " start failed, precedence is " + precedence + ", e = " + e);
        }
    },
            ACTION_FOR_STOP = entry -> {
                BlueLifecycle blueLifecycle = entry.getValue();
                try {
                    blueLifecycle.stop();
                    LOGGER.info("stop() success, {} stopped, precedence is {}", entry.getKey(), blueLifecycle.stopPrecedence());
                } catch (Exception e) {
                    String beanName = entry.getKey();
                    int precedence = blueLifecycle.startPrecedence();
                    LOGGER.error("stop() failed, {} stop failed, precedence is {}, e = {}", beanName, precedence, e);
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "stop() failed, " + beanName + " stop failed, precedence is " + precedence + ", e = " + e);
                }
            };

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        if (!running.compareAndSet(false, true))
            return;

        beans = applicationContext.getBeansOfType(BlueLifecycle.class);

        LOGGER.info("start(), beans = {}", beans);
        beans
                .entrySet()
                .stream()
                .sorted(COMPARATOR_FOR_START)
                .forEach(ACTION_FOR_START);

        LOGGER.info("start(), beans start...");
    }

    @Override
    public void stop() {
        if (!running.compareAndSet(true, false))
            return;

        LOGGER.info("stop(), beans = {}", beans);
        beans
                .entrySet()
                .stream()
                .sorted(COMPARATOR_FOR_STOP)
                .forEach(ACTION_FOR_STOP);

        LOGGER.info("stop(), beans stop...");
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

}
