package com.blue.finance.service.inter;


import com.blue.finance.repository.entity.FinanceAccount;

import java.util.Optional;

/**
 * finance account service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface FinanceAccountService {

    /**
     * create a finance account
     *
     * @param financeAccount
     */
    void insertFinanceAccount(FinanceAccount financeAccount);

    /**
     * init finance account for a member
     *
     * @param memberId
     */
    void insertInitFinanceAccount(Long memberId);

    /**
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId);

}
