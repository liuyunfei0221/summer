package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailConf;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.internal.MailerRegularBuilderImpl;
import reactor.util.Logger;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * mail sender
 *
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "AlibabaLowerCamelCaseVariableNaming"})
public final class MailSender {

    private static final Logger LOGGER = getLogger(MailSender.class);

    private final boolean withDKIM;

    private byte[] domainKey;

    private String domain;

    private String selector;

    private final boolean async;

    private final Mailer MAILER;

    public MailSender(MailConf conf) {
        confAsserter(conf);

        MailerRegularBuilderImpl builder =
                MailerBuilder.withSMTPServer(conf.getSmtpServerHost(), conf.getSmtpServerPort(), conf.getSmtpUsername(), conf.getSmtpPassword());

        ofNullable(conf.getEmailValidator())
                .ifPresent(builder::withEmailValidator);

        ofNullable(conf.getExecutorService())
                .ifPresent(builder::withExecutorService);

        ofNullable(conf.getConnectionPoolCoreSize()).filter(n -> n > 0)
                .ifPresent(builder::withConnectionPoolCoreSize);

        ofNullable(conf.getConnectionPoolMaxSize()).filter(n -> n > 0)
                .ifPresent(builder::withConnectionPoolMaxSize);

        ofNullable(conf.getConnectionPoolClaimTimeoutMillis()).filter(m -> m > 0)
                .ifPresent(builder::withConnectionPoolClaimTimeoutMillis);

        ofNullable(conf.getConnectionPoolExpireAfterMillis()).filter(m -> m > 0)
                .ifPresent(builder::withConnectionPoolExpireAfterMillis);

        ofNullable(conf.getSessionTimeout()).filter(t -> t > 0)
                .ifPresent(builder::withSessionTimeout);

        ofNullable(conf.getConnectionPoolLoadBalancingStrategy())
                .ifPresent(builder::withConnectionPoolLoadBalancingStrategy);

        ofNullable(conf.getTransportStrategy())
                .ifPresent(builder::withTransportStrategy);

        this.async = ofNullable(conf.getAsync()).orElse(true);

        ofNullable(conf.getDebugLogging())
                .ifPresent(builder::withDebugLogging);

        ofNullable(conf.getProperties())
                .ifPresent(m -> m.forEach(builder::withProperty));

        this.MAILER = builder.buildMailer();

        this.withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);

        if (this.withDKIM)
            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(conf.getDomainKeyFile())) {
                if (inputStream == null)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain key media is empty");
                this.domainKey = inputStream.readAllBytes();
                this.domain = conf.getDomain();
                this.selector = conf.getSelector();
            } catch (Exception e) {
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "load domain key media error");
            }
    }


    /**
     * assert params
     *
     * @param conf
     */
    private static void confAsserter(MailConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

        if (isBlank(conf.getSmtpServerHost()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpServerHost can't be blank");

        Integer smtpServerPort = conf.getSmtpServerPort();
        if (smtpServerPort == null || smtpServerPort < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpServerPort can't be null or less than 1");

        if (isBlank(conf.getSmtpUsername()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpUsername can't be blank");

        if (isBlank(conf.getSmtpPassword()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpPassword can't be blank");

        Boolean withDKIM = conf.getWithDKIM();
        if (withDKIM != null && withDKIM) {
            if (isBlank(conf.getDomainKeyFile()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain key media path can't be blank");

            if (isBlank(conf.getDomain()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain can't be blank");

            if (isBlank(conf.getSelector()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "selector can't be blank");
        }

    }

    /**
     * DKIM
     *
     * @param emailPopulatingBuilder
     */
    public void signWithDomainKey(EmailPopulatingBuilder emailPopulatingBuilder) {
        if (withDKIM)
            emailPopulatingBuilder.signWithDomainKey(domainKey, domain, selector);
    }

    /**
     * send mail
     *
     * @param email
     * @return
     */
    public CompletableFuture<Void> sendMail(Email email) {
        return this.sendMail(email, this.async);
    }

    /**
     * send mail
     *
     * @param email
     * @param async
     * @return
     */
    public CompletableFuture<Void> sendMail(Email email, boolean async) {
        LOGGER.info("CompletableFuture<Void> sendMail(Email email, boolean async), email = {}, async = {}", email, async);

        if (email == null)
            throw new BlueException(BAD_REQUEST);

        return MAILER.sendMail(email, async);
    }

}
