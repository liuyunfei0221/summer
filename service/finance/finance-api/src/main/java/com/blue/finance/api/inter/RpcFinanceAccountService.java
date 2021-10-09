package com.blue.finance.api.inter;


/**
 * rpc finance interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RpcFinanceAccountService {


    /**
     * init finance account for member
     *
     * @param memberId
     */
    void insertInitFinanceAccount(Long memberId);

}
