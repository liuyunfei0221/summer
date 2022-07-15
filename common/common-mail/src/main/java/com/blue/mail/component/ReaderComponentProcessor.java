package com.blue.mail.component;

import com.blue.mail.api.conf.MailReaderConf;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;
import reactor.util.Logger;

import java.util.Properties;

import static com.blue.basic.common.base.BlueChecker.*;
import static jakarta.mail.Session.getInstance;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class ReaderComponentProcessor {

    private static final Logger LOGGER = getLogger(ReaderComponentProcessor.class);

    private static final String PROTOCOL = "imap";

    /**
     * generate a session
     *
     * @param conf
     * @return
     */
    public static Session generateSession(MailReaderConf conf) {
        confAsserter(conf);

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            Properties props = new Properties();
            props.put("mail.imap.ssl.socketFactory", sf);
            props.putAll(conf.getProps());

            return getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(conf.getUser(), conf.getPassword());
                }
            });
        } catch (Exception e) {
            LOGGER.error("Session generateSession(MailReaderConf mailReaderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * connect store
     *
     * @param session
     * @return
     */
    public static Store generateStore(Session session) {
        if (isNull(session))
            throw new RuntimeException("session can't be null");

        try {
            Store store = session.getStore(PROTOCOL);
            store.connect();

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
     * @param folderName
     * @return
     */
    public static Folder openFolder(Store store, String folderName) {
        if (isNull(store) || isBlank(folderName))
            throw new RuntimeException("store or mailReaderConf can't be null");

        try {
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
     * assert params
     *
     * @param conf
     */
    public static void confAsserter(MailReaderConf conf) {
        if (isNull(conf))
            throw new RuntimeException("conf can't be null");

        if (isBlank(conf.getUser()))
            throw new RuntimeException("user can't be blank");

        if (isBlank(conf.getPassword()))
            throw new RuntimeException("password can't be blank");

        if (isEmpty(conf.getProps()))
            throw new RuntimeException("props can't be empty");

        if (isBlank(conf.getFolderName()))
            throw new RuntimeException("folder name can't be empty");

        Integer maxWaitingMillisForRefresh = conf.getMaxWaitingMillisForRefresh();
        if (isNull(maxWaitingMillisForRefresh) || maxWaitingMillisForRefresh < 1)
            throw new RuntimeException("maxWaitingMillisForRefresh can't be null or empty");
    }

}
