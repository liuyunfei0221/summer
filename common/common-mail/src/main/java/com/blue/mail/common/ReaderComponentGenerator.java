package com.blue.mail.common;

import com.blue.mail.api.conf.MailReaderConf;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

import static java.util.Optional.ofNullable;

@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "SpellCheckingInspection"})
public final class ReaderComponentGenerator {

    private static final String PROTOCOL = "imaps";

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
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static Store generateStore(Session session, MailReaderConf mailReaderConf) {
        if (session == null || mailReaderConf == null)
            throw new RuntimeException();

        try {
            Store store = session.getStore(PROTOCOL);
            store.connect(mailReaderConf.getImapHost(), mailReaderConf.getImapPort(), mailReaderConf.getUser(), mailReaderConf.getPassword());

            return store;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static Folder openFolder(Store store, MailReaderConf mailReaderConf) {
        if (store == null || mailReaderConf == null)
            throw new RuntimeException();

        try {
            String folderName = mailReaderConf.getFolderName();
            Folder folder = store.getFolder(folderName);

            if (!folder.exists())
                throw new RuntimeException("folder with name (" + folderName + ") is not exist");

            folder.open(Folder.READ_WRITE);

            return folder;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static Folder generateFolder(MailReaderConf mailReaderConf) {
        if (mailReaderConf == null)
            throw new RuntimeException();

        try {
            Session session = generateSession(mailReaderConf);
            Store store = generateStore(session, mailReaderConf);

            return openFolder(store, mailReaderConf);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
