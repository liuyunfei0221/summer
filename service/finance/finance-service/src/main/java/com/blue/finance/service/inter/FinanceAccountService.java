package com.blue.finance.service.inter;


import com.blue.finance.api.model.FinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * finance account service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface FinanceAccountService {

    /**
     * create a finance account
     *
     * @param financeAccount
     * @return
     */
    int insertFinanceAccount(FinanceAccount financeAccount);

    /**
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId);

    /**
     * get balance by member id
     *
     * @param memberId
     * @return
     */
    Mono<FinanceInfo> getBalanceByMemberId(Long memberId);

}
