package com.blue.risk.component.risk.impl;

import com.blue.basic.constant.risk.RiskType;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskHit;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.event.producer.IllegalMarkProducer;
import reactor.util.Logger;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
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
    public RiskAsserted handleEvent(DataEvent dataEvent, RiskAsserted riskAsserted) {
        if (isNull(riskAsserted))
            riskAsserted = new RiskAsserted();

        String ip = dataEvent.getData(CLIENT_IP.key);

        if (Integer.parseInt(dataEvent.getData(RESPONSE_STATUS.key)) != 200) {
            i++;
            if (i % 9 == 0) {
                try {
                    illegalMarkProducer.send(new IllegalMarkEvent(EMPTY_VALUE.value, ip, REQ_RES_KEY_GENERATOR.apply(
                            ofNullable(dataEvent.getData(METHOD.key)).map(String::valueOf).orElse(EMPTY_VALUE.value),
                            ofNullable(dataEvent.getData(URI.key)).map(String::valueOf).orElse(EMPTY_VALUE.value)), true, 20L));

                    riskAsserted.getHits().add(new RiskHit(null, ip, REQ_RES_KEY_GENERATOR.apply(
                            ofNullable(dataEvent.getData(METHOD.key)).map(String::valueOf).orElse(EMPTY_VALUE.value),
                            ofNullable(dataEvent.getData(URI.key)).map(String::valueOf).orElse(EMPTY_VALUE.value)),
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
    public RiskType targetType() {
        return RISK_TYPE;
    }

}
