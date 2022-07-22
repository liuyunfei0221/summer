package com.blue.marketing.service.inter;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.YearAndMonthParam;
import com.blue.marketing.api.model.RewardDateRelationInfo;
import com.blue.marketing.api.model.RewardDateRelationManagerInfo;
import com.blue.marketing.model.RewardDateRelationBatchInsertParam;
import com.blue.marketing.model.RewardDateRelationCondition;
import com.blue.marketing.model.RewardDateRelationInsertParam;
import com.blue.marketing.model.RewardDateRelationUpdateParam;
import com.blue.marketing.repository.entity.RewardDateRelation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * sign reward today relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RewardDateRelationService {

    /**
     * insert a new relation
     *
     * @param rewardDateRelationInsertParam
     * @param operatorId
     * @return
     */
    RewardDateRelationInfo insertRewardDateRelation(RewardDateRelationInsertParam rewardDateRelationInsertParam, Long operatorId);

    /**
     * insert relation by year and month
     *
     * @param rewardDateRelationBatchInsertParam
     * @param operatorId
     * @return
     */
    List<RewardDateRelationInfo> insertRewardDateRelationMonoByYearAndMonth(RewardDateRelationBatchInsertParam rewardDateRelationBatchInsertParam, Long operatorId);

    /**
     * update a exist relation
     *
     * @param rewardDateRelationUpdateParam
     * @param operatorId
     * @return
     */
    RewardDateRelationInfo updateRewardDateRelation(RewardDateRelationUpdateParam rewardDateRelationUpdateParam, Long operatorId);

    /**
     * delete relation
     *
     * @param id
     * @return
     */
    RewardDateRelationInfo deleteRewardDateRelation(Long id);

    /**
     * delete relation by reward id
     *
     * @param rewardId
     * @return
     */
    Integer deleteRewardDateRelationByRewardId(Long rewardId);

    /**
     * select relation by id
     *
     * @param id
     * @return
     */
    Optional<RewardDateRelation> getRewardDateRelation(Long id);

    /**
     * get relation mono by role id
     *
     * @param id
     * @return
     */
    Mono<RewardDateRelation> getRewardDateRelationMono(Long id);

    /**
     * select relation by date
     *
     * @param year
     * @param month
     * @return
     */
    List<RewardDateRelation> selectRewardDateRelationByYearAndMonth(Integer year, Integer month);

    /**
     * select relation mono by date
     *
     * @param year
     * @param month
     * @return
     */
    Mono<List<RewardDateRelation>> selectRewardDateRelationMonoByYearAndMonth(Integer year, Integer month);

    /**
     * select relation by ids
     *
     * @param ids
     * @return
     */
    List<RewardDateRelation> selectRewardDateRelationByIds(List<Long> ids);

    /**
     * select relation mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RewardDateRelation>> selectRewardDateRelationMonoByIds(List<Long> ids);

    /**
     * select relation by page and condition
     *
     * @param limit
     * @param rows
     * @param rewardDateRelationCondition
     * @return
     */
    Mono<List<RewardDateRelation>> selectRewardDateRelationMonoByLimitAndCondition(Long limit, Long rows, RewardDateRelationCondition rewardDateRelationCondition);

    /**
     * count relation by condition
     *
     * @param rewardDateRelationCondition
     * @return
     */
    Mono<Long> countRewardDateRelationMonoByCondition(RewardDateRelationCondition rewardDateRelationCondition);

    /**
     * select relation manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RewardDateRelationManagerInfo>> selectRewardManagerInfoPageMonoByPageAndCondition(PageModelRequest<RewardDateRelationCondition> pageModelRequest);

    /**
     * select relation manager info by year and month
     *
     * @param yearAndMonthParam
     * @return
     */
    Mono<List<RewardDateRelationManagerInfo>> selectRewardDateRelationMonoByYearAndMonth(YearAndMonthParam yearAndMonthParam);

}
