package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailReaderConf;

import javax.mail.*;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountListener;
import javax.mail.search.SearchTerm;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;

@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "unused", "SpellCheckingInspection"})
public final class MailReader {

    private MailReaderConf mailReaderConf;

    private final long DEFAULT_MAX_WAITING_MILLIS_FOR_REFRESH = 5000;

    private Folder folder;

    public MailReader(MailReaderConf mailReaderConf) {
        this.mailReaderConf = mailReaderConf;
        this.folder = FOLDER_GENERATOR.apply(this.mailReaderConf);
        ofNullable(mailReaderConf.getMaxWaitingMillisForRefresh())
                .filter(v -> v > 0)
                .ifPresent(v -> MAX_WAITING_MILLIS_FOR_REFRESH = v);
    }

    private final Function<MailReaderConf, Folder> FOLDER_GENERATOR = ReaderComponentGenerator::generateFolder;

    private volatile boolean refreshing = false;
    private long MAX_WAITING_MILLIS_FOR_REFRESH = 5000;

    private final Supplier<Folder> FOLDER_SUP = () -> {
        if (refreshing) {
            long start = currentTimeMillis();
            while (refreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_MILLIS_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR);
                onSpinWait();
            }
        }

        return folder;
    };

    private final Supplier<Folder> FOLDER_REFRESHER = () -> {
        if (!refreshing)
            synchronized (this) {
                if (!refreshing) {
                    refreshing = true;
                    this.folder = FOLDER_GENERATOR.apply(this.mailReaderConf);
                    refreshing = false;
                }
            }

        return FOLDER_SUP.get();
    };


    public int getMessageCount() {
        try {
            return FOLDER_SUP.get().getMessageCount();
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                try {
                    return FOLDER_REFRESHER.get().getMessageCount();
                } catch (MessagingException ex) {
                    throw new RuntimeException();
                }
            }
            throw new RuntimeException();
        }
    }

    public int getNewMessageCount() {
        try {
            return FOLDER_SUP.get().getNewMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public int getUnreadMessageCount() {
        try {
            return FOLDER_SUP.get().getUnreadMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public int getDeletedMessageCount() {
        try {
            return FOLDER_SUP.get().getDeletedMessageCount();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message getMessage(int index) {
        try {
            return FOLDER_SUP.get().getMessage(index);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages(int start, int end) {
        try {
            return FOLDER_SUP.get().getMessages(start, end);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages(int[] msgnums) {
        try {
            return FOLDER_SUP.get().getMessages(msgnums);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] getMessages() {
        try {
            return FOLDER_SUP.get().getMessages();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void appendMessages(Message[] messages) {
        try {
            FOLDER_SUP.get().appendMessages(messages);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void fetch(Message[] msgs, FetchProfile fp) {
        try {
            FOLDER_SUP.get().fetch(msgs, fp);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(Message[] msgs, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(msgs, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(int start, int end, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(start, end, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void setFlags(int[] msgnums, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(msgnums, flag, value);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void copyMessages(Message[] msgs, Folder folder) {
        try {
            FOLDER_SUP.get().copyMessages(msgs, folder);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] expunge() {
        try {
            return FOLDER_SUP.get().expunge();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] search(SearchTerm term) {
        try {
            return FOLDER_SUP.get().search(term);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public Message[] search(SearchTerm term, Message[] msgs) {
        try {
            return FOLDER_SUP.get().search(term, msgs);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void addConnectionListener(ConnectionListener l) {
        try {
            FOLDER_SUP.get().addConnectionListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeConnectionListener(ConnectionListener l) {
        try {
            FOLDER_SUP.get().removeConnectionListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addFolderListener(FolderListener l) {
        try {
            FOLDER_SUP.get().addFolderListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeFolderListener(FolderListener l) {
        try {
            FOLDER_SUP.get().removeFolderListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addMessageCountListener(MessageCountListener l) {
        try {
            FOLDER_SUP.get().addMessageCountListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeMessageCountListener(MessageCountListener l) {
        try {
            FOLDER_SUP.get().removeMessageCountListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addMessageChangedListener(MessageChangedListener l) {
        try {
            FOLDER_SUP.get().addMessageChangedListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeMessageChangedListener(MessageChangedListener l) {
        try {
            FOLDER_SUP.get().removeMessageChangedListener(l);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
