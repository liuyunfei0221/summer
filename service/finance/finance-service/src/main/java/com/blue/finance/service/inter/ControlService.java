package com.blue.finance.service.inter;

import com.blue.finance.api.model.MemberFinanceInfo;

/**
 * config finance
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ControlService {

    /**
     * init finance info for a new member
     *
     * @param memberFinanceInfo
     */
    void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo);

}
