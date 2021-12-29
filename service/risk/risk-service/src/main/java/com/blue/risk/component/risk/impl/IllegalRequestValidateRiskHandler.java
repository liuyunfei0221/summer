package com.blue.risk.component.risk.impl;

import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.event.producer.IllegalMarkProducer;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.RESPONSE_STATUS;
import static com.blue.risk.component.risk.HandlerPrecedence.ILLEGAL_REQUEST_VALIDATE;
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
            if (i % 3 == 0) {
                illegalMarkProducer.send(new IllegalMarkEvent("", ip, "", true));
                LOGGER.error("test mark");
            }
        }

    }

    @Override
    public int precedence() {
        return ILLEGAL_REQUEST_VALIDATE.precedence;
    }

}
