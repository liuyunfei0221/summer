package com.blue.marketing.service.inter;


import com.blue.marketing.repository.entity.Reward;

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
     * select reward by id
     *
     * @param id
     * @return
     */
    Optional<Reward> getReward(Long id);

    /**
     * select rewards by ids
     *
     * @param ids
     * @return
     */
    List<Reward> selectRewardByIds(List<Long> ids);

}
