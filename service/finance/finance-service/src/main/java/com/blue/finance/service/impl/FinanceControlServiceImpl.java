package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.finance.service.inter.FinanceControlService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SyncKeyPrefix.FINANCE_ACCOUNT_INSERT_SYNC_PRE;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * finance control service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceControlServiceImpl implements FinanceControlService {

    private static final Logger LOGGER = getLogger(FinanceControlServiceImpl.class);

    private RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final SynchronizedProcessor synchronizedProcessor;

    private final FinanceAccountService financeAccountService;

    public FinanceControlServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, SynchronizedProcessor synchronizedProcessor, FinanceAccountService financeAccountService) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.synchronizedProcessor = synchronizedProcessor;
        this.financeAccountService = financeAccountService;
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

    /**
     * init finance account for a new member
     *
     * @param memberId
     */
    @Override
    public FinanceAccountInfo initFinanceAccount(Long memberId) {
        LOGGER.info("memberId = {}", memberId);

        return synchronizedProcessor.handleSupWithSync(FINANCE_ACCOUNT_INSERT_SYNC_KEY_GEN.apply(memberId), () -> {
            FinanceAccountInfo financeAccountInfo = financeAccountService.insertFinanceAccount(INIT_FINANCE_ACCT_GEN.apply(memberId));
            LOGGER.info("financeAccountInfo = {}", financeAccountInfo);

            return financeAccountInfo;
        });
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

        return financeAccountService.getFinanceAccountInfo(id);
    }

    /**
     * get finance account mono by member id with auto init
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return financeAccountService.getFinanceAccountInfoByMemberId(memberId)
                .switchIfEmpty(defer(() -> justOrEmpty(this.initFinanceAccount(memberId))))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

}
