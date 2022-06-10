package com.blue.finance.remote.provider;

import com.blue.finance.api.inter.RpcFinanceControlService;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.finance.service.inter.FinanceControlService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;

/**
 * rpc finance provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "FieldCanBeLocal", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcFinanceControlService.class,
        version = "1.0",
        methods = {
                @Method(name = "initMemberFinanceInfo", async = false)
        })
public class RpcFinanceControlServiceProvider implements RpcFinanceControlService {

    private static final Logger LOGGER = getLogger(RpcFinanceControlServiceProvider.class);

    private final FinanceControlService financeControlService;

    private final ExecutorService executorService;

    public RpcFinanceControlServiceProvider(FinanceControlService financeControlService, ExecutorService executorService) {
        this.financeControlService = financeControlService;
        this.executorService = executorService;
    }

    /**
     * init finance info for member
     *
     * @param memberFinanceInfo
     */
    @Override
    public void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo) {
        LOGGER.info("void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo), memberFinanceInfo = {}", memberFinanceInfo);
        financeControlService.initMemberFinanceInfo(memberFinanceInfo);
    }
}
