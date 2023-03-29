package com.blue.risk.component.risk.impl;

import com.blue.basic.constant.risk.RiskType;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.api.model.RiskHit;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.service.inter.RiskControlService;
import org.slf4j.Logger;

import java.util.function.Consumer;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.risk.RiskType.INVALID_ACCESS_TOO_FREQUENT;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * test risk handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class IllegalRequestValidateRiskHandler implements RiskHandler {

    private static final Logger LOGGER = getLogger(IllegalRequestValidateRiskHandler.class);

    private final RiskControlService riskControlService;

    public IllegalRequestValidateRiskHandler(RiskControlService riskControlService) {
        this.riskControlService = riskControlService;
    }

    private static final RiskType RISK_TYPE = INVALID_ACCESS_TOO_FREQUENT;

    private boolean enable = false;

    private final Consumer<RiskStrategyInfo> STRATEGY_ASSERTER = riskStrategyInfo -> {
        if (isNull(riskStrategyInfo))
            throw new BlueException(EMPTY_PARAM);
        riskStrategyInfo.asserts();

        //TODO

    };

    int i = 0;

    @Override
    public RiskAsserted handle(RiskEvent riskEvent, RiskAsserted riskAsserted) {
        if (isNull(riskAsserted))
            riskAsserted = new RiskAsserted();

        String ip = riskEvent.getClientIp();

        if (200 != riskEvent.getResponseStatus()) {
            i++;
            if (i % 5 == 0) {
                try {
                    RiskHit riskHit = new RiskHit(riskEvent.getMemberId(), ip, riskEvent.getMethod(), riskEvent.getUri(), RISK_TYPE.identity,
                            20L, false, false, TIME_STAMP_GETTER.get());

                    //treatment measures
                    riskControlService.illegalMarkByEvent(new IllegalMarkEvent(
                                    ofNullable(riskHit.getMemberId()).map(String::valueOf).orElse(EMPTY_VALUE.value)
                                    , ip, riskHit.getMethod(), riskHit.getUri(), true, 20L))
                            .subscribe(success -> LOGGER.info("success = {}", success));

                    //result
                    riskAsserted.setHit(true);
                    riskAsserted.setInterrupt(false);
                    riskAsserted.getHits().add(riskHit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                LOGGER.error("test mark");
            }
        }

        return riskAsserted;
    }

    @Override
    public RiskAsserted validate(RiskEvent riskEvent, RiskAsserted riskAsserted) {
        if (isNull(riskAsserted))
            riskAsserted = new RiskAsserted();

        String ip = riskEvent.getClientIp();

        if (200 != riskEvent.getResponseStatus()) {
            i++;
            if (i % 9 == 0) {
                try {
                    //result
                    riskAsserted.setHit(true);
                    riskAsserted.setInterrupt(false);
                    riskAsserted.getHits().add(new RiskHit(null, ip, riskEvent.getMethod(), riskEvent.getUri(),
                            RISK_TYPE.identity, 20L, false, false, TIME_STAMP_GETTER.get()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                LOGGER.error("test mark");
            }
        }

        return riskAsserted;
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void assertStrategy(RiskStrategyInfo riskStrategyInfo) {
        LOGGER.error("riskStrategyInfo = {}", riskStrategyInfo);
        STRATEGY_ASSERTER.accept(riskStrategyInfo);
    }

    @Override
    public boolean updateStrategy(RiskStrategyInfo riskStrategyInfo) {
        LOGGER.error("riskStrategyInfo = {}", riskStrategyInfo);

        ofNullable(riskStrategyInfo.getEnable())
                .ifPresent(en -> this.enable = en);

        return true;
    }

    @Override
    public RiskType targetType() {
        return RISK_TYPE;
    }

}