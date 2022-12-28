package com.blue.finance.service.inter;


import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import reactor.core.publisher.Mono;

/**
 * finance account service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface FinanceAccountService {

    /**
     * init finance account for a new member
     *
     * @param financeAccount
     */
    FinanceAccountInfo insertFinanceAccount(FinanceAccount financeAccount);

    /**
     * query finance account mono by id
     *
     * @param id
     * @return
     */
    Mono<FinanceAccountInfo> getFinanceAccountInfo(Long id);

    /**
     * get finance account mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId);

}
