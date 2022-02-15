package com.blue.risk.component.risk.impl;

import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.event.producer.IllegalMarkProducer;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.risk.component.risk.HandlerPrecedence.ILLEGAL_REQUEST_VALIDATE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/29
 * @apiNote
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
            System.err.println(ip);
            if (i % 9 == 0) {
                illegalMarkProducer.send(new IllegalMarkEvent("", ip, REQ_RES_KEY_GENERATOR.apply(
                        ofNullable(dataEvent.getData(METHOD.key)).map(String::valueOf).orElse(""),
                        ofNullable(dataEvent.getData(URI.key)).map(String::valueOf).orElse("")), true, 20L));
                LOGGER.error("test mark");
            }
        }

    }

    @Override
    public int precedence() {
        return ILLEGAL_REQUEST_VALIDATE.precedence;
    }

}
