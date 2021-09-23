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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Status.VALID;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 财务业务实现
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
     * 根据成员主键获取资金账户余额信息
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<FinanceInfo> getBalanceByMemberId(Long memberId) {
        LOGGER.info("getBalanceByMemberId(Long memberId), memberId = {}", memberId);

        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员主键不能为空或小于1");
        return just(memberId)
                .flatMap(mi -> {
                    Optional<FinanceAccount> faOpt = financeAccountService.getFinanceAccountByMemberId(mi);
                    if (faOpt.isEmpty())
                        return error(new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "资金账户不存在"));

                    FinanceAccount financeAccount = faOpt.get();
                    if (VALID.status != financeAccount.getStatus())
                        return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "资金账户已冻结"));

                    return just(financeAccount);
                })
                .flatMap(fa ->
                        just(new FinanceInfo(fa.getBalance())));
    }

}
