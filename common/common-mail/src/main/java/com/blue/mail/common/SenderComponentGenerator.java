package com.blue.mail.common;

import com.blue.mail.api.conf.MailSenderConf;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import reactor.util.Logger;

import java.util.Properties;

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

            props.put("mail.smtp.ssl.socketFactory", sf);

            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.host", mailSenderConf.getSmtpServerHost());
            props.put("mail.smtp.port", mailSenderConf.getSmtpServerPort());

            props.put("mail.smtp.ssl", mailSenderConf.getMailSmtpSsl());
            props.put("mail.smtp.starttls.enable", mailSenderConf.getMailSmtpStarttlsEnable());

            return Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mailSenderConf.getSmtpUsername(), mailSenderConf.getSmtpPassword());
                        }
                    });

        } catch (Exception e) {
            LOGGER.error("Session generateSession(MailSenderConf mailSenderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

}
