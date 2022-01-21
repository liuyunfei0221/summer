package com.blue.mail.common;

import com.blue.mail.api.conf.MailReaderConf;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.event.*;
import javax.mail.search.SearchTerm;
import java.util.*;

@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MailReader {

    private String folderName = "INBOX";

    private Session session;

    private Store store;

    private Folder folder;


    public MailReader(MailReaderConf conf) {

        this.session = generateSession();
        this.store = generateStore();
        this.folder = this.openFolder(folderName);

    }

    private Session generateSession() {

        try {
            String protocol = "imaps";
            String host = "outlook.office365.com";
            int port = 993;

            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
//        sf.setSecureRandom();

            props.put("mail.imap.ssl.socketFactory", sf);

            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", String.valueOf(port));
            props.setProperty("mail.imapStore.protocol", protocol);
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.debug", "false");

            return Session.getInstance(props);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Store generateStore() {
        try {
            String protocol = "imaps";
            String host = "outlook.office365.com";
            int port = 993;
            String username = "yunfei0221@outlook.com";
            String password = "Fei19890116";

            Store store = this.session.getStore(protocol);
            store.connect(host, port, username, password);

            return store;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Folder openFolder(String folderName) {
        try {
            Folder folder = store.getFolder(folderName);

            if (!folder.exists())
                throw new RuntimeException("folder with name (" + folderName + ") is not exist");

            folder.open(Folder.READ_WRITE);

            return folder;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public int getMessageCount() {
        try {
            return folder.getMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public int getNewMessageCount() {
        try {
            return folder.getNewMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public int getUnreadMessageCount() {
        try {
            return folder.getUnreadMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public int getDeletedMessageCount() {
        try {
            return folder.getDeletedMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message getMessage(int index) {
        try {
            return folder.getMessage(index);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages(int start, int end) {
        try {
            return folder.getMessages(start, end);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages(int[] msgnums) {
        try {
            return folder.getMessages(msgnums);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages() {
        try {
            return folder.getMessages();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void appendMessages(Message[] messages) {
        try {
            folder.appendMessages(messages);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void fetch(Message[] msgs, FetchProfile fp) {
        try {
            folder.fetch(msgs, fp);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(Message[] msgs, Flags flag, boolean value) {
        try {
            folder.setFlags(msgs, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(int start, int end, Flags flag, boolean value) {
        try {
            folder.setFlags(start, end, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(int[] msgnums, Flags flag, boolean value) {
        try {
            folder.setFlags(msgnums, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void copyMessages(Message[] msgs, Folder folder) {
        try {
            folder.copyMessages(msgs, folder);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] expunge() {
        try {
            return folder.expunge();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] search(SearchTerm term) {
        try {
            return folder.search(term);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] search(SearchTerm term, Message[] msgs) {
        try {
            return folder.search(term, msgs);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void addConnectionListener(ConnectionListener l) {
        try {
            folder.addConnectionListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeConnectionListener(ConnectionListener l) {
        try {
            folder.removeConnectionListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addFolderListener(FolderListener l) {
        try {
            folder.addFolderListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeFolderListener(FolderListener l) {
        try {
            folder.removeFolderListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addMessageCountListener(MessageCountListener l) {
        try {
            folder.addMessageCountListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeMessageCountListener(MessageCountListener l) {
        try {
            folder.removeMessageCountListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addMessageChangedListener(MessageChangedListener l) {
        try {
            folder.addMessageChangedListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeMessageChangedListener(MessageChangedListener l) {
        try {
            folder.removeMessageChangedListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
