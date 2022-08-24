package com.blue.marketing.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.SignInService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REWARDS_REFRESH;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * refresh rewards consumer
 *
 * @author liuyunfei
 */
public final class RewardsRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RewardsRefreshConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final SignInService signInService;

    private BluePulsarListener<EmptyEvent> pulsarListener;

    public RewardsRefreshConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, SignInService signInService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.signInService = signInService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> dataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> just(ee).publishOn(scheduler)
                                .then(signInService.refreshDayRewards())
                                .doOnError(throwable -> LOGGER.info("signInService.refreshDayRewards() failed, ee = {}, throwable = {}", ee, throwable))
                                .subscribe(ig -> LOGGER.info("signInService.refreshDayRewards(), ig = {}, ee = {}", ig, ee)));

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(pulsarClient, blueConsumerConfig.getByKey(REWARDS_REFRESH.name), dataConsumer);
    }

    @Override
    public int startPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public void start() {
        this.pulsarListener.run();
        LOGGER.warn("pulsarListener start...");
    }

    @Override
    public void stop() {
        this.pulsarListener.shutdown();
        LOGGER.warn("pulsarListener shutdown...");
    }

}
