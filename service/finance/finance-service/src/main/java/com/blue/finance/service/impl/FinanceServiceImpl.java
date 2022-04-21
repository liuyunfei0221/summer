package com.blue.finance.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.FinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.finance.service.inter.FinanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * finance service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceServiceImpl implements FinanceService {

    private static final Logger LOGGER = getLogger(FinanceServiceImpl.class);

    private final FinanceAccountService financeAccountService;

    public FinanceServiceImpl(FinanceAccountService financeAccountService) {
        this.financeAccountService = financeAccountService;
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
                    Optional<FinanceAccount> faOpt = financeAccountService.getFinanceAccountByMemberId(mi);
                    if (faOpt.isEmpty()) {
                        LOGGER.error("A member did not allocate finance account, please repair data, memberId = {}", memberId);
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    }

                    FinanceAccount financeAccount = faOpt.get();
                    if (isInvalidStatus(financeAccount.getStatus()))
                        return error(() -> new BlueException(ACCOUNT_HAS_BEEN_FROZEN));

                    return just(financeAccount);
                })
                .flatMap(fa ->
                        just(new FinanceInfo(fa.getBalance())));
    }

}
