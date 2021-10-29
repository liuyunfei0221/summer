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

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.common.base.Asserter.isInvalidStatus;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
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

        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);
        return just(memberId)
                .flatMap(mi -> {
                    Optional<FinanceAccount> faOpt = financeAccountService.getFinanceAccountByMemberId(mi);
                    if (faOpt.isEmpty()) {
                        LOGGER.error("A member did not allocate funds account, please repair data, memberId = {}", memberId);
                        return error(new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "The finance account does not exist, please contact customer service."));
                    }

                    FinanceAccount financeAccount = faOpt.get();
                    if (isInvalidStatus(financeAccount.getStatus()))
                        return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "Your finance account has been frozen"));

                    return just(financeAccount);
                })
                .flatMap(fa ->
                        just(new FinanceInfo(fa.getBalance())));
    }

}
