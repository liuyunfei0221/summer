package com.blue.verify.service.inter;

import reactor.core.publisher.Mono;

/**
 * sms service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface SmsService {

    /**
     * send sms verify
     *
     * @param phone
     * @param text
     * @return
     */
    Mono<Boolean> send(String phone, String text);

}
