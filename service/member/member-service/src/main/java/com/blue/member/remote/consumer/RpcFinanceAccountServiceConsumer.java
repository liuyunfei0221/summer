package com.blue.member.remote.consumer;

import com.blue.finance.api.inter.RpcFinanceAccountService;
import com.blue.finance.api.model.MemberFinanceInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;

/**
 * rpc finance consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal"})
@Component
public class RpcFinanceAccountServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcFinanceAccountServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-finance"},
            methods = {
                    @Method(name = "initMemberFinanceInfo", async = false)
            })
    private RpcFinanceAccountService rpcFinanceAccountService;

    private final ExecutorService executorService;

    public RpcFinanceAccountServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * init finance info for member
     *
     * @param memberFinanceInfo
     */
    public void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo) {
        LOGGER.info("void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo), memberFinanceInfo = {}", memberFinanceInfo);
        rpcFinanceAccountService.initMemberFinanceInfo(memberFinanceInfo);
    }

}
