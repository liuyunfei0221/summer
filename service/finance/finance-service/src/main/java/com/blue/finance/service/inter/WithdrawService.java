package com.blue.finance.service.inter;

import com.blue.base.model.common.Access;
import com.blue.finance.api.model.WithdrawInfo;

/**
 * withdraw service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface WithdrawService {

    /**
     * withdraw
     *
     * @param withdrawInfo
     * @param access
     * @return
     */
    Boolean withdraw(WithdrawInfo withdrawInfo, Access access);

}
