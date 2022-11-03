package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.SignInReward;
import com.blue.marketing.api.model.MonthSignInRewardRecord;
import reactor.core.publisher.Mono;

/**
 * sign in service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface SignInService {

    /**
     * refresh day rewards
     *
     * @return
     */
    Mono<Void> refreshDayRewards();

    /**
     * sign in today
     *
     * @param memberId
     * @return
     */
    Mono<SignInReward> signIn(Long memberId);

    /**
     * query sign in records
     *
     * @param memberId
     * @return
     */
    Mono<MonthSignInRewardRecord> getSignInRecord(Long memberId);

}
