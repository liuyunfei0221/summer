package com.blue.verify.service.impl;

import com.blue.verify.service.inter.MailService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * mail service impl
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = getLogger(MailServiceImpl.class);

    /**
     * send email verify
     *
     * @param email
     * @param text
     * @return
     */
    @Override
    public Mono<Boolean> send(String email, String text) {

        LOGGER.warn("send email verify, email = {}, text = {}", email, text);

        return just(true);
    }

}
