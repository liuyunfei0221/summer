package com.blue.risk.component.risk.impl;

import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.event.producer.IllegalMarkProducer;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.basic.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.risk.component.risk.constant.HandlerPrecedence.ILLEGAL_REQUEST_VALIDATE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class IllegalRequestValidateRiskHandler implements RiskHandler {

    private static final Logger LOGGER = getLogger(IllegalRequestValidateRiskHandler.class);

    //private final JwtProcessor<MemberPayload> jwtProcessor;

    private final IllegalMarkProducer illegalMarkProducer;

    public IllegalRequestValidateRiskHandler(IllegalMarkProducer illegalMarkProducer) {
        this.illegalMarkProducer = illegalMarkProducer;
    }

    int i = 0;

    @Override
    public void handleEvent(DataEvent dataEvent) {
        String ip = dataEvent.getData(CLIENT_IP.key);

        if (Integer.parseInt(dataEvent.getData(RESPONSE_STATUS.key)) != 200) {
            i++;
            if (i % 32 == 0) {
                illegalMarkProducer.send(new IllegalMarkEvent(EMPTY_DATA.value, ip, REQ_RES_KEY_GENERATOR.apply(
                        ofNullable(dataEvent.getData(METHOD.key)).map(String::valueOf).orElse(EMPTY_DATA.value),
                        ofNullable(dataEvent.getData(URI.key)).map(String::valueOf).orElse(EMPTY_DATA.value)), true, 20L));
                LOGGER.error("test mark");
            }
        }

    }

    @Override
    public int precedence() {
        return ILLEGAL_REQUEST_VALIDATE.precedence;
    }

}
