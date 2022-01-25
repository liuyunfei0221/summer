package com.blue.mail.common;

import com.blue.mail.api.conf.MailSenderConf;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import reactor.util.Logger;

import java.util.Properties;

import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
public final class SenderComponentGenerator {

    private static final Logger LOGGER = getLogger(SenderComponentGenerator.class);

    /**
     * generate a session
     *
     * @param mailSenderConf
     * @return
     */
    public static Session generateSession(MailSenderConf mailSenderConf) {
        try {
            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            props.put("mail.imap.ssl.socketFactory", sf);

            props.setProperty("mail.smtp.host", mailSenderConf.getSmtpServerHost());
            props.setProperty("mail.smtp.port", String.valueOf(mailSenderConf.getSmtpServerPort()));
            props.setProperty("mail.smtp.ssl", String.valueOf(ofNullable(mailSenderConf.getMailSmtpSsl()).orElse(true)));
            props.setProperty("mail.smtp.starttls.enable", String.valueOf(ofNullable(mailSenderConf.getMailSmtpStarttlsEnable()).orElse(false)));

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mailSenderConf.getSmtpUsername(), mailSenderConf.getSmtpPassword());
                        }
                    });

            return Session.getInstance(props);
        } catch (Exception e) {
            LOGGER.error("Session generateSession(MailSenderConf mailSenderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

}
