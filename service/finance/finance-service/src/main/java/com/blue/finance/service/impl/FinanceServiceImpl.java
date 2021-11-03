package com.blue.finance.service.impl;

import com.blue.finance.api.model.FinanceInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.finance.service.inter.FinanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.Asserter.isInvalidStatus;
import static com.blue.base.common.base.Asserter.isValidIdentity;
import static com.blue.base.constant.base.CommonException.DATA_NOT_EXIST_EXP;
import static com.blue.base.constant.base.CommonException.INVALID_IDENTITY_EXP;
import static com.blue.finance.constant.FinanceCommonException.ACCOUNT_HAS_BEEN_FROZEN_EXP;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * finance service impl
 *
 * @author DarkBlue
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
        LOGGER.info("getBalanceByMemberId(Long memberId), memberId = {}", memberId);

        if (isValidIdentity(memberId))
            return just(memberId)
                    .flatMap(mi -> {
                        Optional<FinanceAccount> faOpt = financeAccountService.getFinanceAccountByMemberId(mi);
                        if (faOpt.isEmpty()) {
                            LOGGER.error("A member did not allocate funds account, please repair data, memberId = {}", memberId);
                            return error(DATA_NOT_EXIST_EXP.exp);
                        }

                        FinanceAccount financeAccount = faOpt.get();
                        if (isInvalidStatus(financeAccount.getStatus()))
                            return error(ACCOUNT_HAS_BEEN_FROZEN_EXP.exp);

                        return just(financeAccount);
                    })
                    .flatMap(fa ->
                            just(new FinanceInfo(fa.getBalance())));

        throw INVALID_IDENTITY_EXP.exp;
    }

}
