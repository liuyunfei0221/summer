package com.blue.marketing.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.EmptyEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.SignInService;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REWARDS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * refresh rewards consumer
 *
 * @author liuyunfei
 */
public final class RewardsRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RewardsRefreshConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final SignInService signInService;

    private BluePulsarConsumer<EmptyEvent> rewardsRefreshConsumer;

    public RewardsRefreshConsumer(BlueConsumerConfig blueConsumerConfig, SignInService signInService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.signInService = signInService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> rewardsRefreshDataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> {
                            LOGGER.info("rewardsRefreshConsumer received");
                            signInService.refreshDayRewards()
                                    .doOnError(throwable -> LOGGER.info("signInService.refreshDayRewards() failed, throwable = {}", throwable))
                                    .subscribe(v -> LOGGER.info("signInService.refreshDayRewards()"));
                        });

        this.rewardsRefreshConsumer = generateConsumer(blueConsumerConfig.getByKey(REWARDS_REFRESH.name), rewardsRefreshDataConsumer);
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
        this.rewardsRefreshConsumer.run();
        LOGGER.warn("rewardsRefreshConsumer start...");
    }

    @Override
    public void stop() {
        this.rewardsRefreshConsumer.shutdown();
        LOGGER.warn("rewardsRefreshConsumer shutdown...");
    }

}
