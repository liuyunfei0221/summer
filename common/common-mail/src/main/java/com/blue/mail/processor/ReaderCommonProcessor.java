package com.blue.mail.processor;

import com.blue.mail.api.conf.MailReaderConf;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;
import org.slf4j.Logger;

import java.util.Properties;

import static com.blue.basic.common.base.BlueChecker.*;
import static jakarta.mail.Session.getInstance;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class ReaderCommonProcessor {

    private static final Logger LOGGER = getLogger(ReaderCommonProcessor.class);

    private static final String PROTOCOL = "imap";

    /**
     * generate a session
     *
     * @param conf
     * @return
     */
    public static Session generateSession(MailReaderConf conf) {
        assertConf(conf);

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
            LOGGER.error("generateSession failed, e = {}", e);
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
            LOGGER.error("generateStore failed, e = {}", e);
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
            LOGGER.error("openFolder failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * assert params
     *
     * @param conf
     */
    public static void assertConf(MailReaderConf conf) {
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
