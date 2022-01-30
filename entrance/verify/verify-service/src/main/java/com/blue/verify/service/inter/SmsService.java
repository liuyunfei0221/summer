package com.blue.verify.service.inter;

import reactor.core.publisher.Mono;

/**
 * sms service
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface SmsService {

    /**
     * send sms
     *
     * @param phone
     * @param text
     * @return
     */
    Mono<Boolean> send(String phone, String text);

}
