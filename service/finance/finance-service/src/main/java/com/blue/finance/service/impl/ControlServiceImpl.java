package com.blue.finance.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.service.inter.ControlService;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.util.Loggers.getLogger;

/**
 * control service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ControlServiceImpl implements ControlService {

    private static final Logger LOGGER = getLogger(ControlServiceImpl.class);

    private final FinanceAccountService financeAccountService;

    private BlueIdentityProcessor blueIdentityProcessor;

    public ControlServiceImpl(FinanceAccountService financeAccountService, BlueIdentityProcessor blueIdentityProcessor) {
        this.financeAccountService = financeAccountService;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    private final Function<Long, FinanceAccount> INIT_FINANCE_ACCT_GEN = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        FinanceAccount financeAccount = new FinanceAccount();

        financeAccount.setId(blueIdentityProcessor.generate(FinanceAccount.class));
        financeAccount.setMemberId(memberId);
        financeAccount.setBalance(0L);
        financeAccount.setFrozen(0L);
        financeAccount.setIncome(0L);
        financeAccount.setOutlay(0L);
        financeAccount.setStatus(Status.VALID.status);

        long epochSecond = Instant.now().getEpochSecond();
        financeAccount.setCreateTime(epochSecond);
        financeAccount.setUpdateTime(epochSecond);

        return financeAccount;
    };

    /**
     * init finance infos for a new member
     *
     * @param memberFinanceInfo
     */
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    @Override
    public void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo) {
        LOGGER.info("void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo), memberFinanceInfo = {}", memberFinanceInfo);
        if (isNull(memberFinanceInfo))
            throw new BlueException(EMPTY_PARAM);

        financeAccountService.insertFinanceAccount(INIT_FINANCE_ACCT_GEN.apply(memberFinanceInfo.getMemberId()));
    }

}
