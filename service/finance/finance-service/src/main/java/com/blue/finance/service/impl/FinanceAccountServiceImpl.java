package com.blue.finance.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(FinanceAccountMapper financeAccountMapper) {
        this.financeAccountMapper = financeAccountMapper;
    }

    /**
     * create a finance account
     *
     * @param financeAccount
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @Override
    public int insertFinanceAccount(FinanceAccount financeAccount) {
        LOGGER.info("insertFinanceAccount(FinanceAccount financeAccount), financeAccount = {}", financeAccount);
        return financeAccountMapper.insert(financeAccount);

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
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId) {
        LOGGER.info("getFinanceAccountByMemberId(Long memberId), memberId = {}", memberId);

        if (isValidIdentity(memberId))
            return ofNullable(financeAccountMapper.getByMemberId(memberId));

        throw new BlueException(INVALID_IDENTITY);
    }

}
