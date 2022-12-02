package com.blue.risk.remote.provider;

import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.inter.RpcRiskService;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.service.inter.RiskAnalyzeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.concurrent.CompletableFuture;

/**
 * rpc risk provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcRiskService.class,
        version = "1.0",
        methods = {
                @Method(name = "handleRiskEvent", async = true),
                @Method(name = "handleDataEvent", async = true),
                @Method(name = "validateRiskEvent", async = true),
                @Method(name = "validateDataEvent", async = true)
        })
public class RpcRiskServiceProvider implements RpcRiskService {

    private final RiskAnalyzeService riskAnalyzeService;

    public RpcRiskServiceProvider(RiskAnalyzeService riskAnalyzeService) {
        this.riskAnalyzeService = riskAnalyzeService;
    }

    /**
     * handle event
     *
     * @param riskEvent
     * @return
     */
    @Override
    public CompletableFuture<RiskAsserted> handleRiskEvent(RiskEvent riskEvent) {
        return riskAnalyzeService.handleRiskEvent(riskEvent).toFuture();
    }

    /**
     * handle event
     *
     * @param dataEvent
     * @return
     */
    @Override
    public CompletableFuture<RiskAsserted> handleDataEvent(DataEvent dataEvent) {
        return riskAnalyzeService.handleDataEvent(dataEvent).toFuture();
    }

    /**
     * validate event
     *
     * @param riskEvent
     * @return
     */
    @Override
    public CompletableFuture<RiskAsserted> validateRiskEvent(RiskEvent riskEvent) {
        return riskAnalyzeService.validateRiskEvent(riskEvent).toFuture();
    }

    /**
     * validate event
     *
     * @param dataEvent
     * @return
     */
    @Override
    public CompletableFuture<RiskAsserted> validateDataEvent(DataEvent dataEvent) {
        return riskAnalyzeService.validateDataEvent(dataEvent).toFuture();
    }

}
