package com.blue.marketing.service.inter;


import com.blue.marketing.repository.entity.SignRewardTodayRelation;

import java.util.List;

/**
 * sign reward today relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface SignRewardTodayRelationService {

    /**
     * select reward-date-relation by date
     *
     * @param year
     * @param month
     * @return
     */
    List<SignRewardTodayRelation> selectRelationByYearAndMonth(Integer year, Integer month);

}
