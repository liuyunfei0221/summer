package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailConf;
import com.blue.mail.api.param.Mail;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class MailSender {

    private byte[] domainKey;

    private String domain;

    private String selector;

    private final boolean async;

    private final Mailer MAILER;


    public MailSender(MailConf conf) {
        confAsserter(conf);

        this.domainKey = null;

        this.domain = null;

        this.selector = null;

        this.async = true;

        this.MAILER = null;

    }

    private final Function<Mail, Email> MAIL_CONVERTER = mail ->
            EmailBuilder.startingBlank()
                    .signWithDomainKey(domainKey, domain, selector)
                    .buildEmail();

    /**
     * assert params
     *
     * @param conf
     */
    private static void confAsserter(MailConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");


    }

    /**
     * send mail
     *
     * @param mail
     * @return
     */
    CompletableFuture<Void> sendMail(Mail mail) {
        return this.sendMail(mail, this.async);
    }

    /**
     * send mail
     *
     * @param mail
     * @param async
     * @return
     */
    CompletableFuture<Void> sendMail(Mail mail, boolean async) {
        if (mail == null)
            throw new BlueException(BAD_REQUEST);


        return MAILER.sendMail(MAIL_CONVERTER.apply(mail), async);
    }

}
