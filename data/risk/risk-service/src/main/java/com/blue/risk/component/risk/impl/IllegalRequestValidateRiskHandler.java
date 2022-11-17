package com.blue.risk.component.risk.impl;

import com.blue.basic.constant.risk.RiskType;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.api.model.RiskHit;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.event.producer.IllegalMarkProducer;
import reactor.util.Logger;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.risk.RiskType.INVALID_ACCESS_TOO_FREQUENT;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * test risk handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class IllegalRequestValidateRiskHandler implements RiskHandler {

    private static final Logger LOGGER = getLogger(IllegalRequestValidateRiskHandler.class);

    private final IllegalMarkProducer illegalMarkProducer;

    public IllegalRequestValidateRiskHandler(IllegalMarkProducer illegalMarkProducer) {
        this.illegalMarkProducer = illegalMarkProducer;
    }

    private static final RiskType RISK_TYPE = INVALID_ACCESS_TOO_FREQUENT;

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
                    RiskHit riskHit = new RiskHit(riskEvent.getMemberId(), ip, riskEvent.getMethod(), riskEvent.getUri(), RISK_TYPE.identity, 20L, TIME_STAMP_GETTER.get());

                    //treatment measures
                    illegalMarkProducer.send(new IllegalMarkEvent(
                            ofNullable(riskHit.getMemberId()).map(String::valueOf).orElse(EMPTY_VALUE.value)
                            , ip, riskHit.getMethod(), riskHit.getUri(), true, 20L));

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
                            RISK_TYPE.identity, 20L, TIME_STAMP_GETTER.get()));
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
        return true;
    }

    @Override
    public void assertStrategy(RiskStrategyInfo riskStrategyInfo) {
        
    }

    @Override
    public boolean updateStrategy(RiskStrategyInfo riskStrategyInfo) {
        return true;
    }

    @Override
    public RiskType targetType() {
        return RISK_TYPE;
    }

}
