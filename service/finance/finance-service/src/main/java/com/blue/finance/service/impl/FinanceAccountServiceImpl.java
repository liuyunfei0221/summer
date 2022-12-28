package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.Consumer;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.finance.converter.FinanceModelConverters.FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
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

    private FinanceAccountMapper financeAccountMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(BlueIdentityProcessor blueIdentityProcessor, FinanceAccountMapper financeAccountMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.financeAccountMapper = financeAccountMapper;
    }

    private final Consumer<FinanceAccount> ITEM_EXIST_VALIDATOR = t -> {
        if (isNull(t))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(t.getMemberId()))
            throw new BlueException(INVALID_PARAM);

        if (isNull(t.getBalance()) || isNull(t.getFrozen()) || isNull(t.getIncome()) || isNull(t.getOutlay()))
            throw new BlueException(INVALID_PARAM);

        assertStatus(t.getStatus(), false);

        if (isNull(t.getCreateTime()) || isNull(t.getUpdateTime()))
            throw new BlueException(INVALID_PARAM);

        if (isNotNull(financeAccountMapper.selectByMemberId(t.getMemberId())))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    /**
     * init finance info for a new member
     *
     * @param financeAccount
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public FinanceAccountInfo insertFinanceAccount(FinanceAccount financeAccount) {
        LOGGER.info("financeAccount = {}", financeAccount);
        if (isNull(financeAccount))
            throw new BlueException(EMPTY_PARAM);

        ITEM_EXIST_VALIDATOR.accept(financeAccount);

        if (isInvalidIdentity(financeAccount.getId()))
            financeAccount.setId(blueIdentityProcessor.generate(FinanceAccount.class));

        financeAccountMapper.insert(financeAccount);

        return FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER.apply(financeAccount);
    }

    /**
     * query finance account mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<FinanceAccountInfo> getFinanceAccountInfo(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(financeAccountMapper.selectByPrimaryKey(id))
                .map(FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

    /**
     * get finance account mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(financeAccountMapper.selectByMemberId(memberId))
                .map(FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER);
    }

}
