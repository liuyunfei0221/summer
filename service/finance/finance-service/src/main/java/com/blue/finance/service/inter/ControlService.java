package com.blue.finance.service.inter;

import com.blue.finance.api.model.MemberFinanceInfo;

/**
 * config finance
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ControlService {

    /**
     * init finance infos for a new member
     *
     * @param memberFinanceInfo
     */
    void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo);

}
