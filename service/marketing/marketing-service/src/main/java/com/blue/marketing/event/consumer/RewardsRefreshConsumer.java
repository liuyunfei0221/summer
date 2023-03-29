package com.blue.marketing.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.SignInService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REWARDS_REFRESH;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

/**
 * refresh rewards consumer
 *
 * @author liuyunfei
 */
public final class RewardsRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RewardsRefreshConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final SignInService signInService;

    private BluePulsarListener<EmptyEvent> pulsarListener;

    public RewardsRefreshConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, SignInService signInService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.signInService = signInService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> dataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> just(ee)
                                .then(signInService.refreshDayRewards())
                                .doOnError(throwable -> LOGGER.info("signInService.refreshDayRewards() failed, ee = {}, throwable = {}", ee, throwable))
                                .doOnEach(voidSignal -> LOGGER.info("signInService.refreshDayRewards(), voidSignal = {}, ee = {}", voidSignal, ee))
                                .subscribe());

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
