package com.blue.member.remote.consumer;

import com.blue.finance.api.inter.RpcFinanceAccountService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;

/**
 * rpc finance consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal"})
@Component
public class RpcFinanceAccountServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcFinanceAccountServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-finance"}, methods = {
            @Method(name = "insertInitFinanceAccount", async = false)
    })
    private RpcFinanceAccountService rpcFinanceAccountService;

    private final ExecutorService executorService;

    public RpcFinanceAccountServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * init finance account for member
     *
     * @param memberId
     */
    public void insertInitFinanceAccount(Long memberId) {
        LOGGER.info("void insertInitFinanceAccount(Long memberId), memberId = {}", memberId);
        rpcFinanceAccountService.insertInitFinanceAccount(memberId);
    }
}
