package com.blue.marketing.service.inter;


import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;

import java.util.List;
import java.util.Optional;

/**
 * 奖励信息业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RewardService {

    /**
     * 根据主键查询奖励信息
     *
     * @param id
     * @return
     */
    Optional<Reward> getRewardByPrimaryKey(Long id);

    /**
     * 根据主键集合批量查询奖励信息列表
     *
     * @param ids
     * @return
     */
    List<Reward> listRewardByIds(List<Long> ids);

    /**
     * 根据年份及月份查询该月底当日奖励关联信息
     *
     * @param year
     * @param month
     * @return
     */
    List<SignRewardTodayRelation> listRelationByYearAndMonth(Integer year, Integer month);

}
