package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.RewardDateRelationInfo;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.model.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * config reward,relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MarketingControlService {

    /**
     * insert a new reward
     *
     * @param rewardInsertParam
     * @param operatorId
     * @return
     */
    Mono<RewardInfo> insertReward(RewardInsertParam rewardInsertParam, Long operatorId);

    /**
     * update a exist reward
     *
     * @param rewardUpdateParam
     * @param operatorId
     * @return
     */
    Mono<RewardInfo> updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId);

    /**
     * delete reward
     *
     * @param id
     * @return
     */
    Mono<RewardInfo> deleteReward(Long id);

    /**
     * insert a new relation
     *
     * @param rewardDateRelationInsertParam
     * @param operatorId
     * @return
     */
    Mono<RewardDateRelationInfo> insertRewardDateRelation(RewardDateRelationInsertParam rewardDateRelationInsertParam, Long operatorId);

    /**
     * insert relation by year and month
     *
     * @param rewardDateRelationBatchInsertParam
     * @param operatorId
     * @return
     */
    Mono<List<RewardDateRelationInfo>> insertRewardDateRelationMonoByYearAndMonth(RewardDateRelationBatchInsertParam rewardDateRelationBatchInsertParam, Long operatorId);

    /**
     * update a exist relation
     *
     * @param rewardDateRelationUpdateParam
     * @param operatorId
     * @return
     */
    Mono<RewardDateRelationInfo> updateRewardDateRelation(RewardDateRelationUpdateParam rewardDateRelationUpdateParam, Long operatorId);

    /**
     * delete relation
     *
     * @param id
     * @return
     */
    Mono<RewardDateRelationInfo> deleteRewardDateRelation(Long id);

}
