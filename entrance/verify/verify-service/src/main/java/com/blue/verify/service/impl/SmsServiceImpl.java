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
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = getLogger(SmsServiceImpl.class);

    /**
     * send sms
     *
     * @param phone
     * @param text
     * @return
     */
    @Override
    public Mono<Boolean> send(String phone, String text) {

        LOGGER.warn("send a sms, phone = {}, text = {}", phone, text);

        return just(true);
    }

}
