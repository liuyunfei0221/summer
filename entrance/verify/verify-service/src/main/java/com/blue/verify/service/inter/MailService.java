package com.blue.verify.service.inter;

import reactor.core.publisher.Mono;

/**
 * mail service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MailService {

    /**
     * send email verify
     *
     * @param email
     * @param text
     * @return
     */
    Mono<Boolean> send(String email, String text);

}
