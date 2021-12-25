package com.blue.risk.service.impl;


import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.risk.event.producer.IllegalMarkProducer;
import com.blue.risk.service.inter.RiskService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.RESPONSE_STATUS;
import static reactor.util.Loggers.getLogger;

/**
 * risk analyse service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class RiskServiceImpl implements RiskService {

    private static final Logger LOGGER = getLogger(RiskServiceImpl.class);

    private final IllegalMarkProducer illegalMarkProducer;

    public RiskServiceImpl(IllegalMarkProducer illegalMarkProducer) {
        this.illegalMarkProducer = illegalMarkProducer;
    }

    int i = 0;

    @Override
    public void testMarkIllegal(DataEvent dataEvent) {
        String ip = dataEvent.getData(CLIENT_IP.key);

        if (Integer.parseInt(dataEvent.getData(RESPONSE_STATUS.key)) != 200) {
            i++;
            System.err.println(ip);
            if (i % 3 == 0) {
                illegalMarkProducer.send(new IllegalMarkEvent("", ip, true));
                LOGGER.error("test mark");
            }
        }
    }
}
