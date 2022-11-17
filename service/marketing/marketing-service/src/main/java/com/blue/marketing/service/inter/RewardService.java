package com.blue.marketing.service.inter;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.model.RewardInsertParam;
import com.blue.marketing.model.RewardManagerInfo;
import com.blue.marketing.model.RewardUpdateParam;
import com.blue.marketing.repository.entity.Reward;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * reward service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RewardService {

    /**
     * insert a new reward
     *
     * @param rewardInsertParam
     * @param operatorId
     * @return
     */
    RewardInfo insertReward(RewardInsertParam rewardInsertParam, Long operatorId);

    /**
     * update a exist reward
     *
     * @param rewardUpdateParam
     * @param operatorId
     * @return
     */
    RewardInfo updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId);

    /**
     * delete reward
     *
     * @param id
     * @return
     */
    RewardInfo deleteReward(Long id);

    /**
     * select reward by id
     *
     * @param id
     * @return
     */
    Optional<Reward> getRewardOpt(Long id);

    /**
     * get reward mono by role id
     *
     * @param id
     * @return
     */
    Mono<Reward> getReward(Long id);

    /**
     * select rewards mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Reward>> selectRewardByIds(List<Long> ids);

    /**
     * select reward info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RewardInfo>> selectRewardInfoByIds(List<Long> ids);

    /**
     * select reward by page and condition
     *
     * @param limit
     * @param rows
     * @param rewardCondition
     * @return
     */
    Mono<List<Reward>> selectRewardByLimitAndCondition(Long limit, Long rows, RewardCondition rewardCondition);

    /**
     * count reward by condition
     *
     * @param rewardCondition
     * @return
     */
    Mono<Long> countRewardByCondition(RewardCondition rewardCondition);

    /**
     * select reward info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RewardManagerInfo>> selectRewardManagerInfoPageByPageAndCondition(PageModelRequest<RewardCondition> pageModelRequest);

}
