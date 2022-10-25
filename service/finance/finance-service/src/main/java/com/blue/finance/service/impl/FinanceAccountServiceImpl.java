package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SyncKeyPrefix.FINANCE_ACCOUNT_INSERT_SYNC_PRE;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "BlockingMethodInNonBlockingContext"})
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {

    private static final Logger LOGGER = getLogger(FinanceAccountServiceImpl.class);

    private RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private FinanceAccountMapper financeAccountMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                                     SynchronizedProcessor synchronizedProcessor, FinanceAccountMapper financeAccountMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.financeAccountMapper = financeAccountMapper;
    }

    private static final Function<Long, String> FINANCE_ACCOUNT_INSERT_SYNC_KEY_GEN = memberId -> {
        if (isValidIdentity(memberId))
            return FINANCE_ACCOUNT_INSERT_SYNC_PRE.prefix + memberId;

        throw new BlueException(BAD_REQUEST);
    };

    private final Function<Long, FinanceAccount> INIT_FINANCE_ACCT_GEN = mid -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(INVALID_IDENTITY);

        MemberBasicInfo memberBasicInfo = rpcMemberBasicServiceConsumer.getMemberBasicInfo(mid).toFuture().join();
        if (isNull(memberBasicInfo))
            throw new BlueException(INVALID_PARAM);

        FinanceAccount financeAccount = new FinanceAccount();

        financeAccount.setId(blueIdentityProcessor.generate(FinanceAccount.class));
        financeAccount.setMemberId(mid);
        financeAccount.setBalance(0L);
        financeAccount.setFrozen(0L);
        financeAccount.setIncome(0L);
        financeAccount.setOutlay(0L);

        financeAccount.setStatus(memberBasicInfo.getStatus());

        Long stamp = TIME_STAMP_GETTER.get();
        financeAccount.setCreateTime(stamp);
        financeAccount.setUpdateTime(stamp);

        return financeAccount;
    };

    private final Consumer<FinanceAccount> INSERT_ITEM_VALIDATOR = t -> {
        if (isNull(t))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(t.getId()) || isInvalidIdentity(t.getMemberId()))
            throw new BlueException(INVALID_PARAM);

        if (isNull(t.getBalance()) || isNull(t.getFrozen()) || isNull(t.getIncome()) || isNull(t.getOutlay()))
            throw new BlueException(INVALID_PARAM);

        assertStatus(t.getStatus(), false);

        if (isNull(t.getCreateTime()) || isNull(t.getUpdateTime()))
            throw new BlueException(INVALID_PARAM);

        if (isNotNull(financeAccountMapper.selectByPrimaryKey(t.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        if (isNotNull(financeAccountMapper.selectByMemberId(t.getMemberId())))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    /**
     * init finance info for a new member
     *
     * @param memberId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public FinanceAccount initMemberFinance(Long memberId) {
        LOGGER.info("FinanceAccount initMemberFinance(Long memberId), memberId = {}", memberId);

        FinanceAccount financeAccount = INIT_FINANCE_ACCT_GEN.apply(memberId);

        synchronizedProcessor.handleTaskWithSync(FINANCE_ACCOUNT_INSERT_SYNC_KEY_GEN.apply(memberId), () -> {
            INSERT_ITEM_VALIDATOR.accept(financeAccount);

            int inserted = financeAccountMapper.insert(financeAccount);
            LOGGER.info("inserted = {}", inserted);
        });

        return financeAccount;
    }

    /**
     * query finance account mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<FinanceAccountInfo> getFinanceAccountInfoMono(Long id) {
        LOGGER.info("Mono<FinanceAccountInfo> getFinanceAccountInfoMono(Long id), id = {}", id);
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
    public Mono<FinanceAccountInfo> getFinanceAccountInfoMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<FinanceAccountInfo> getFinanceAccountInfoMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(financeAccountMapper.selectByMemberId(memberId))
                .map(FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> justOrEmpty(this.initMemberFinance(memberId)).map(FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER)))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

}
