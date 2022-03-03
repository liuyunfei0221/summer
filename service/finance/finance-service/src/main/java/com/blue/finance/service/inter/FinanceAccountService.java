package com.blue.finance.service.inter;


import com.blue.finance.repository.entity.FinanceAccount;

import java.util.Optional;

/**
 * finance account service
 *
 * @author DarkBlue
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

}
