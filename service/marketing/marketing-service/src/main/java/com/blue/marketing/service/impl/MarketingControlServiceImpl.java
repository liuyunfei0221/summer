package com.blue.marketing.service.impl;

import com.blue.marketing.api.model.RewardDateRelationInfo;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.event.producer.RewardsRefreshProducer;
import com.blue.marketing.model.*;
import com.blue.marketing.service.inter.MarketingControlService;
import com.blue.marketing.service.inter.RewardDateRelationService;
import com.blue.marketing.service.inter.RewardService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.blue.basic.constant.common.SummerAttr.EMPTY_EVENT;
import static reactor.core.publisher.Mono.just;

/**
 * config reward,relation service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "AliControlFlowStatementWithoutBraces"})
@Service
public class MarketingControlServiceImpl implements MarketingControlService {

    private final RewardsRefreshProducer rewardsRefreshProducer;

    private final RewardService rewardService;

    private final RewardDateRelationService rewardDateRelationService;

    public MarketingControlServiceImpl(RewardsRefreshProducer rewardsRefreshProducer, RewardService rewardService, RewardDateRelationService rewardDateRelationService) {
        this.rewardsRefreshProducer = rewardsRefreshProducer;
        this.rewardService = rewardService;
        this.rewardDateRelationService = rewardDateRelationService;
    }

    /**
     * insert a new reward
     *
     * @param rewardInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RewardInfo> insertReward(RewardInsertParam rewardInsertParam, Long operatorId) {
        return just(rewardService.insertReward(rewardInsertParam, operatorId));
    }

    /**
     * update a exist reward
     *
     * @param rewardUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RewardInfo> updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId) {
        return just(rewardService.updateReward(rewardUpdateParam, operatorId))
                .doOnSuccess(ig -> rewardsRefreshProducer.send(EMPTY_EVENT));
    }

    /**
     * delete reward
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RewardInfo> deleteReward(Long id) {
        return just(rewardDateRelationService.deleteRewardDateRelationByRewardId(id))
                .doOnNext(count -> {
                    if (count > 0)
                        rewardsRefreshProducer.send(EMPTY_EVENT);
                })
                .then(just(rewardService.deleteReward(id)));
    }

    /**
     * insert a new relation
     *
     * @param rewardDateRelationInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RewardDateRelationInfo> insertRewardDateRelation(RewardDateRelationInsertParam rewardDateRelationInsertParam, Long operatorId) {
        return just(rewardDateRelationService.insertRewardDateRelation(rewardDateRelationInsertParam, operatorId))
                .doOnSuccess(ig -> rewardsRefreshProducer.send(EMPTY_EVENT));
    }

    /**
     * insert relation by year and month
     *
     * @param rewardDateRelationBatchInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<List<RewardDateRelationInfo>> insertRewardDateRelationMonoByYearAndMonth(RewardDateRelationBatchInsertParam rewardDateRelationBatchInsertParam, Long operatorId) {
        return just(rewardDateRelationService.insertRewardDateRelationMonoByYearAndMonth(rewardDateRelationBatchInsertParam, operatorId))
                .doOnSuccess(ig -> rewardsRefreshProducer.send(EMPTY_EVENT));
    }

    /**
     * update a exist relation
     *
     * @param rewardDateRelationUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RewardDateRelationInfo> updateRewardDateRelation(RewardDateRelationUpdateParam rewardDateRelationUpdateParam, Long operatorId) {
        return just(rewardDateRelationService.updateRewardDateRelation(rewardDateRelationUpdateParam, operatorId))
                .doOnSuccess(ig -> rewardsRefreshProducer.send(EMPTY_EVENT));
    }

    /**
     * delete relation
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RewardDateRelationInfo> deleteRewardDateRelation(Long id) {
        return just(rewardDateRelationService.deleteRewardDateRelation(id))
                .doOnSuccess(ig -> rewardsRefreshProducer.send(EMPTY_EVENT));
    }

}
