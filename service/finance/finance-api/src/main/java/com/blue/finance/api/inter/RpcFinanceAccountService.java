package com.blue.finance.api.inter;


import com.blue.finance.api.model.FinanceAccountInfo;

import java.util.concurrent.CompletableFuture;

/**
 * rpc finance account interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcFinanceAccountService {

    /**
     * query finance account info by id
     *
     * @param id
     * @return
     */
    CompletableFuture<FinanceAccountInfo> getFinanceAccountInfo(Long id);

    /**
     * get finance account info by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId);

}
