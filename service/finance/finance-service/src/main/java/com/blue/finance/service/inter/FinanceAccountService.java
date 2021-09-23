package com.blue.finance.service.inter;


import com.blue.finance.repository.entity.FinanceAccount;

import java.util.Optional;

/**
 * 资金账户业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface FinanceAccountService {

    /**
     * 新增资金账户
     *
     * @param financeAccount
     */
    void insertFinanceAccount(FinanceAccount financeAccount);

    /**
     * 为成员初始化资金账户
     *
     * @param memberId
     */
    void insertInitFinanceAccount(Long memberId);

    /**
     * 根据成员主键获取资金账户余额信息
     *
     * @param memberId
     * @return
     */
    Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId);

}
