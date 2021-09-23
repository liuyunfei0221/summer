package com.blue.finance.remote.provider;

import com.blue.finance.api.inter.RpcFinanceAccountService;
import com.blue.finance.service.inter.FinanceAccountService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;

/**
 * 资金账户RPC实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "FieldCanBeLocal", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcFinanceAccountService.class, version = "1.0", methods = {
        @Method(name = "insertInitFinanceAccount", async = false)
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
     * 为成员初始化资金账户
     *
     * @param memberId
     */
    @Override
    public void insertInitFinanceAccount(Long memberId) {
        LOGGER.info("void insertInitFinanceAccount(Long memberId), memberId = {}", memberId);
        financeAccountService.insertInitFinanceAccount(memberId);
    }
}
