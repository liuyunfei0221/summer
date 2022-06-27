package com.blue.finance.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.FinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * finance account service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {

    private static final Logger LOGGER = getLogger(FinanceAccountServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final FinanceAccountMapper financeAccountMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(BlueIdentityProcessor blueIdentityProcessor, FinanceAccountMapper financeAccountMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.financeAccountMapper = financeAccountMapper;
    }

    /**
     * create a finance account
     *
     * @param financeAccount
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int insertFinanceAccount(FinanceAccount financeAccount) {
        LOGGER.info("int insertFinanceAccount(FinanceAccount financeAccount), financeAccount = {}", financeAccount);
        if (isNull(financeAccount))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(financeAccount.getId()))
            financeAccount.setId(blueIdentityProcessor.generate(FinanceAccount.class));

        return financeAccountMapper.insert(financeAccount);
    }

    /**
     * get finance account by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId) {
        LOGGER.info("Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(financeAccountMapper.getByMemberId(memberId));
    }

    /**
     * get balance by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<FinanceInfo> getBalanceByMemberId(Long memberId) {
        LOGGER.info("Mono<FinanceInfo> getBalanceByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(memberId)
                .flatMap(mi -> {
                    Optional<FinanceAccount> faOpt = getFinanceAccountByMemberId(mi);
                    if (faOpt.isEmpty()) {
                        LOGGER.error("A member did not allocate finance account, please repair data, memberId = {}", memberId);
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    }

                    FinanceAccount financeAccount = faOpt.get();
                    if (isInvalidStatus(financeAccount.getStatus()))
                        return error(() -> new BlueException(DATA_HAS_BEEN_FROZEN));

                    return just(financeAccount);
                })
                .flatMap(fa ->
                        just(new FinanceInfo(fa.getBalance())));
    }

}
