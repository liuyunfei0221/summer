package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.DayReward;
import com.blue.marketing.api.model.MonthRewardRecord;
import reactor.core.publisher.Mono;

/**
 * 签到业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface SignService {

    /**
     * 当日签到
     *
     * @param memberId
     * @return
     */
    Mono<DayReward> insertSignIn(Long memberId);

    /**
     * 查询当月签到
     *
     * @param memberId
     * @return
     */
    Mono<MonthRewardRecord> getSignInRecord(Long memberId);


}
