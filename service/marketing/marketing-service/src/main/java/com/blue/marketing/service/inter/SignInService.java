package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.SignInReward;
import com.blue.marketing.api.model.MonthRewardRecord;
import reactor.core.publisher.Mono;

/**
 * sign in service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface SignInService {

    /**
     * sign in today
     *
     * @param memberId
     * @return
     */
    Mono<SignInReward> insertSignIn(Long memberId);

    /**
     * query sign in records
     *
     * @param memberId
     * @return
     */
    Mono<MonthRewardRecord> getSignInRecord(Long memberId);


}
