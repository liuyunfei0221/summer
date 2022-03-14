package com.blue.finance.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.service.inter.ControlService;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.time.Instant;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static reactor.util.Loggers.getLogger;

/**
 * control service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ControlServiceImpl implements ControlService {

    private static final Logger LOGGER = getLogger(ControlServiceImpl.class);

    private final FinanceAccountService financeAccountService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    public ControlServiceImpl(FinanceAccountService financeAccountService, BlueIdentityProcessor blueIdentityProcessor) {
        this.financeAccountService = financeAccountService;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * init finance infos for a new member
     *
     * @param memberFinanceInfo
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 60)
    @Override
    public void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo) {
        LOGGER.info("void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo), memberFinanceInfo = {}", memberFinanceInfo);

        if (memberFinanceInfo == null)
            throw new BlueException(EMPTY_PARAM);

        Long memberId = memberFinanceInfo.getMemberId();

        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        long epochSecond = Instant.now().getEpochSecond();

        FinanceAccount financeAccount = new FinanceAccount();

        long id = blueIdentityProcessor.generate(FinanceAccount.class);
        financeAccount.setId(id);

        financeAccount.setMemberId(memberId);
        financeAccount.setBalance(0L);
        financeAccount.setFrozen(0L);
        financeAccount.setIncome(0L);
        financeAccount.setOutlay(0L);
        financeAccount.setStatus(Status.VALID.status);
        financeAccount.setCreateTime(epochSecond);
        financeAccount.setUpdateTime(epochSecond);

        financeAccountService.insertFinanceAccount(financeAccount);

//        if (1 == 1)
//            throw new BlueException(666, 666, "test rollback");
    }

}
