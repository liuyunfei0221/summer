package com.blue.mail.common;

import com.blue.mail.api.conf.MailReaderConf;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Folder;
import jakarta.mail.Session;
import jakarta.mail.Store;
import reactor.util.Logger;

import java.util.Properties;

import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class ReaderComponentGenerator {

    private static final Logger LOGGER = getLogger(ReaderComponentGenerator.class);

    private static final String PROTOCOL = "imap";

    /**
     * generate a session
     *
     * @param mailReaderConf
     * @return
     */
    public static Session generateSession(MailReaderConf mailReaderConf) {
        try {
            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            props.put("mail.imap.ssl.socketFactory", sf);
            props.setProperty("mail.imap.host", mailReaderConf.getImapHost());
            props.setProperty("mail.imap.port", String.valueOf(mailReaderConf.getImapPort()));
            props.setProperty("mail.imapStore.protocol", PROTOCOL);
            props.setProperty("mail.imap.ssl.enable", String.valueOf(ofNullable(mailReaderConf.getImapSslEnable()).orElse(true)));
            props.setProperty("mail.debug", String.valueOf(ofNullable(mailReaderConf.getDebug()).orElse(false)));

            return Session.getInstance(props);
        } catch (Exception e) {
            LOGGER.error("Session generateSession(MailReaderConf mailReaderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * connect store
     *
     * @param session
     * @param mailReaderConf
     * @return
     */
    public static Store generateStore(Session session, MailReaderConf mailReaderConf) {
        if (session == null || mailReaderConf == null)
            throw new RuntimeException("session or mailReaderConf can't be null");

        try {
            Store store = session.getStore(PROTOCOL);
            store.connect(mailReaderConf.getImapHost(), mailReaderConf.getImapPort(), mailReaderConf.getUser(), mailReaderConf.getPassword());

            return store;
        } catch (Exception e) {
            LOGGER.error("Store generateStore(Session session, MailReaderConf mailReaderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * open a folder
     *
     * @param store
     * @param mailReaderConf
     * @return
     */
    public static Folder openFolder(Store store, MailReaderConf mailReaderConf) {
        if (store == null || mailReaderConf == null)
            throw new RuntimeException("store or mailReaderConf can't be null");

        try {
            String folderName = mailReaderConf.getFolderName();
            Folder folder = store.getFolder(folderName);


            if (!folder.exists())
                throw new RuntimeException("folder with name (" + folderName + ") is not exist");

            folder.open(Folder.READ_WRITE);

            return folder;
        } catch (Exception e) {
            LOGGER.error("Folder openFolder(Store store, MailReaderConf mailReaderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * generate a folder
     *
     * @param mailReaderConf
     * @return
     */
    public static Folder generateFolder(MailReaderConf mailReaderConf) {
        if (mailReaderConf == null)
            throw new RuntimeException("mailReaderConf can't be null");

        try {
            Session session = generateSession(mailReaderConf);
            Store store = generateStore(session, mailReaderConf);

            return openFolder(store, mailReaderConf);
        } catch (Exception e) {
            LOGGER.error("Folder generateFolder(MailReaderConf mailReaderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

}
