package com.blue.finance.remote.provider;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.inter.RpcFinanceAccountService;
import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.service.inter.FinanceAccountService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.blue.basic.constant.common.ResponseElement.DATA_NOT_EXIST;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * rpc finance account provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "FieldCanBeLocal"})
@DubboService(interfaceClass = RpcFinanceAccountService.class,
        version = "1.0",
        methods = {
                @Method(name = "getFinanceAccountInfo", async = true),
                @Method(name = "getFinanceAccountInfoByMemberId", async = true)
        })
public class RpcFinanceAccountServiceProvider implements RpcFinanceAccountService {

    private static final Logger LOGGER = getLogger(RpcFinanceAccountServiceProvider.class);

    private final FinanceAccountService financeAccountService;

    private final ExecutorService executorService;

    public RpcFinanceAccountServiceProvider(FinanceAccountService financeAccountService, ExecutorService executorService) {
        this.financeAccountService = financeAccountService;
        this.executorService = executorService;
    }

    /**
     * query finance account info by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<FinanceAccountInfo> getFinanceAccountInfo(Long id) {
        return just(id)
                .flatMap(financeAccountService::getFinanceAccountInfo)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    }

    /**
     * get finance account info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId) {
        return just(memberId)
                .flatMap(financeAccountService::getFinanceAccountInfoByMemberId)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    }

}
