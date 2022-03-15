package com.blue.verify.service.impl;

import com.blue.verify.service.inter.SmsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * sms service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = getLogger(SmsServiceImpl.class);

    /**
     * send sms verify
     *
     * @param phone
     * @param text
     * @return
     */
    @Override
    public Mono<Boolean> send(String phone, String text) {

        LOGGER.warn("send sms verify, phone = {}, text = {}", phone, text);

        return just(true);
    }

}
