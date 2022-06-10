package com.blue.finance.api.inter;


import com.blue.finance.api.model.MemberFinanceInfo;

/**
 * rpc finance interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RpcFinanceControlService {


    /**
     * init finance info for member
     *
     * @param memberFinanceInfo
     */
    void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo);

}
