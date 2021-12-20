package com.blue.finance.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.common.BlueIdentityProcessor;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.time.Instant;
import java.util.Optional;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.common.base.Asserter.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * finance account service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {

    private static final Logger LOGGER = getLogger(FinanceAccountServiceImpl.class);

    private final FinanceAccountMapper financeAccountMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(FinanceAccountMapper financeAccountMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.financeAccountMapper = financeAccountMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * create a finance account
     *
     * @param financeAccount
     */
    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    @Override
    public void insertFinanceAccount(FinanceAccount financeAccount) {
        LOGGER.info("insertFinanceAccount(FinanceAccount financeAccount), financeAccount = {}", financeAccount);
        financeAccountMapper.insert(financeAccount);

        //test business exp
        //if (1 == 1) {
        //    throw new BlueException(500, 999, "test rollback");
        //}

        //test runtime exp
        //if (1 == 1) {
        //    throw new RuntimeException("test rollback");
        //}
    }

    /**
     * init finance account for a member
     *
     * @param memberId
     */
    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    @Override
    public void insertInitFinanceAccount(Long memberId) {
        LOGGER.info("insertInitFinanceAccount(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId)) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);
        }

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

        insertFinanceAccount(financeAccount);
    }

    /**
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId) {
        LOGGER.info("getFinanceAccountByMemberId(Long memberId), memberId = {}", memberId);

        if (isValidIdentity(memberId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return ofNullable(financeAccountMapper.getByMemberId(memberId));
    }

}
