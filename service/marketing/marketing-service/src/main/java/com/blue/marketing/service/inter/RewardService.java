package com.blue.marketing.service.inter;


import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;

import java.util.List;
import java.util.Optional;

/**
 * reward service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RewardService {

    /**
     * select reward by id
     *
     * @param id
     * @return
     */
    Optional<Reward> getRewardByPrimaryKey(Long id);

    /**
     * select rewards by ids
     *
     * @param ids
     * @return
     */
    List<Reward> listRewardByIds(List<Long> ids);

    /**
     * select reward-date-relation by date
     *
     * @param year
     * @param month
     * @return
     */
    List<SignRewardTodayRelation> listRelationByYearAndMonth(Integer year, Integer month);

}
