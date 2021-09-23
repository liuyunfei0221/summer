package com.blue.finance.api.inter;


/**
 * 资金账户相关RPC接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RpcFinanceAccountService {


    /**
     * 为成员初始化资金账户
     *
     * @param memberId
     */
    void insertInitFinanceAccount(Long memberId);

}
