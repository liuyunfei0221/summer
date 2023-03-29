package com.blue.finance.service.impl;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.WithdrawInfo;
import com.blue.finance.service.inter.WithdrawService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * withdraw service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class WithdrawServiceImpl implements WithdrawService {

    private static final Logger LOGGER = getLogger(WithdrawServiceImpl.class);

    /**
     * withdraw
     *
     * @param withdrawInfo
     * @param access
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean withdraw(WithdrawInfo withdrawInfo, Access access) {
        LOGGER.info("withdrawInfo = {}, access = {}", withdrawInfo, access);
        if (isNull(withdrawInfo) || isNull(access))
            throw new BlueException(BAD_REQUEST);
        withdrawInfo.asserts();

        System.err.println(withdrawInfo);
        System.err.println();
        System.err.println(access);

        return true;
    }

}
