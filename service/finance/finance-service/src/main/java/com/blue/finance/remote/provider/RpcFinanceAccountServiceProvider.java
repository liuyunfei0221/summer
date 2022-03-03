package com.blue.finance.remote.provider;

import com.blue.finance.api.inter.RpcFinanceAccountService;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.finance.service.inter.ControlService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;

/**
 * rpc finance provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "FieldCanBeLocal", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcFinanceAccountService.class, version = "1.0", methods = {
        @Method(name = "initMemberFinanceInfo", async = false)
})
public class RpcFinanceAccountServiceProvider implements RpcFinanceAccountService {

    private static final Logger LOGGER = getLogger(RpcFinanceAccountServiceProvider.class);

    private final ControlService controlService;

    private final ExecutorService executorService;

    public RpcFinanceAccountServiceProvider(ControlService controlService, ExecutorService executorService) {
        this.controlService = controlService;
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
        controlService.initMemberFinanceInfo(memberFinanceInfo);
    }
}
