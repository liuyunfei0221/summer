package com.blue.finance.service.inter;

import com.blue.finance.api.model.FinanceInfo;
import reactor.core.publisher.Mono;

/**
 * finance service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface FinanceService {

    /**
     * get balance by member id
     *
     * @param memberId
     * @return
     */
    Mono<FinanceInfo> getBalanceByMemberId(Long memberId);

}
