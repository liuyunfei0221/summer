package com.blue.marketing.service.inter;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.api.model.RewardManagerInfo;
import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.model.RewardInsertParam;
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
     * insert a new role
     *
     * @param rewardInsertParam
     * @param operatorId
     * @return
     */
    RewardInfo insertReward(RewardInsertParam rewardInsertParam, Long operatorId);

    /**
     * update a exist role
     *
     * @param rewardUpdateParam
     * @param operatorId
     * @return
     */
    RewardInfo updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId);

    /**
     * delete role
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
    Optional<Reward> getReward(Long id);

    /**
     * get role mono by role id
     *
     * @param id
     * @return
     */
    Mono<Reward> getRewardMono(Long id);

    /**
     * select rewards by ids
     *
     * @param ids
     * @return
     */
    List<Reward> selectRewardByIds(List<Long> ids);

    /**
     * select roles mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Reward>> selectRewardMonoByIds(List<Long> ids);

    /**
     * select all roles
     *
     * @return
     */
    Mono<List<Reward>> selectReward();

    /**
     * select role by page and condition
     *
     * @param limit
     * @param rows
     * @param rewardCondition
     * @return
     */
    Mono<List<Reward>> selectRewardMonoByLimitAndCondition(Long limit, Long rows, RewardCondition rewardCondition);

    /**
     * count role by condition
     *
     * @param rewardCondition
     * @return
     */
    Mono<Long> countRewardMonoByCondition(RewardCondition rewardCondition);

    /**
     * select role info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RewardManagerInfo>> selectRewardManagerInfoPageMonoByPageAndCondition(PageModelRequest<RewardCondition> pageModelRequest);

}
