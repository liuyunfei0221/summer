package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailReaderConf;
import jakarta.mail.*;
import jakarta.mail.event.ConnectionListener;
import jakarta.mail.event.FolderListener;
import jakarta.mail.event.MessageChangedListener;
import jakarta.mail.event.MessageCountListener;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.SearchTerm;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;

@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "unused", "SpellCheckingInspection", "DuplicatedCode"})
public final class MailReader {

    private MailReaderConf mailReaderConf;

    private Folder folder;

    public MailReader(MailReaderConf mailReaderConf) {
        this.mailReaderConf = mailReaderConf;
        this.folder = FOLDER_GENERATOR.apply(this.mailReaderConf);
        this.maxWaitingMillisForRefresh = mailReaderConf.getMaxWaitingMillisForRefresh();
    }

    private final Function<MailReaderConf, Folder> FOLDER_GENERATOR = ReaderComponentGenerator::generateFolder;

    private volatile boolean refreshing = false;
    private long maxWaitingMillisForRefresh;

    private final Supplier<Folder> FOLDER_SUP = () -> {
        if (refreshing) {
            long start = currentTimeMillis();
            while (refreshing) {
                if (currentTimeMillis() - start > maxWaitingMillisForRefresh)
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

    private final Predicate<Exception> REFRESH_PREDICATE = e ->
            e instanceof IllegalStateException;


    public static void parseMessage(Message message) {
        try {
            String subject = message.getSubject();
            String from = (message.getFrom()[0]).toString();
            System.err.println("邮件的主题：" + subject);
            System.err.println("邮件的发件人地址：" + from);

            String contentType = message.getContentType();
            System.err.println("邮件的内容类型：" + contentType);

            if (contentType.startsWith("multipart")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

                int count = mimeMultipart.getCount();
                System.err.println("邮件的parts：" + count);

                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    Object content = bodyPart.getContent();

                    System.err.println("邮件的内容" + i + ": " + content);
                }
            } else {
                Object content = message.getContent();
                System.err.println("邮件的内容：" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //=============================================================================================

    public int getMessageCount() {
        try {
            return FOLDER_SUP.get().getMessageCount();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getMessageCount();
                } catch (MessagingException ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public int getNewMessageCount() {
        try {
            return FOLDER_SUP.get().getNewMessageCount();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getNewMessageCount();
                } catch (MessagingException ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public int getUnreadMessageCount() {
        try {
            return FOLDER_SUP.get().getUnreadMessageCount();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getUnreadMessageCount();
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public int getDeletedMessageCount() {
        try {
            return FOLDER_SUP.get().getUnreadMessageCount();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getUnreadMessageCount();
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message getMessage(int index) {
        try {
            return FOLDER_SUP.get().getMessage(index);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getMessage(index);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] getMessages(int start, int end) {
        try {
            return FOLDER_SUP.get().getMessages(start, end);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getMessages(start, end);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] getMessages(int[] msgnums) {
        try {
            return FOLDER_SUP.get().getMessages(msgnums);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getMessages(msgnums);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] getMessages() {
        try {
            return FOLDER_SUP.get().getMessages();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().getMessages();
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void appendMessages(Message[] messages) {
        try {
            FOLDER_SUP.get().appendMessages(messages);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().appendMessages(messages);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void fetch(Message[] msgs, FetchProfile fp) {
        try {
            FOLDER_SUP.get().fetch(msgs, fp);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().fetch(msgs, fp);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void setFlags(Message[] msgs, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(msgs, flag, value);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().setFlags(msgs, flag, value);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void setFlags(int start, int end, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(start, end, flag, value);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().setFlags(start, end, flag, value);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void setFlags(int[] msgnums, Flags flag, boolean value) {
        try {
            FOLDER_SUP.get().setFlags(msgnums, flag, value);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().setFlags(msgnums, flag, value);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void copyMessages(Message[] msgs, Folder folder) {
        try {
            FOLDER_SUP.get().copyMessages(msgs, folder);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().copyMessages(msgs, folder);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] expunge() {
        try {
            return FOLDER_SUP.get().expunge();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().expunge();
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] search(SearchTerm term) {
        try {
            return FOLDER_SUP.get().search(term);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().search(term);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public Message[] search(SearchTerm term, Message[] msgs) {
        try {
            return FOLDER_SUP.get().search(term, msgs);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_REFRESHER.get().search(term, msgs);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void addConnectionListener(ConnectionListener l) {
        try {
            FOLDER_SUP.get().addConnectionListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().addConnectionListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void removeConnectionListener(ConnectionListener l) {
        try {
            FOLDER_SUP.get().removeConnectionListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().removeConnectionListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void addFolderListener(FolderListener l) {
        try {
            FOLDER_SUP.get().addFolderListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().addFolderListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void removeFolderListener(FolderListener l) {
        try {
            FOLDER_SUP.get().removeFolderListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().removeFolderListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void addMessageCountListener(MessageCountListener l) {
        try {
            FOLDER_SUP.get().addMessageCountListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().addMessageCountListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void removeMessageCountListener(MessageCountListener l) {
        try {
            FOLDER_SUP.get().removeMessageCountListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().removeMessageCountListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void addMessageChangedListener(MessageChangedListener l) {
        try {
            FOLDER_SUP.get().addMessageChangedListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().addMessageChangedListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

    public void removeMessageChangedListener(MessageChangedListener l) {
        try {
            FOLDER_SUP.get().removeMessageChangedListener(l);
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    FOLDER_REFRESHER.get().removeMessageChangedListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

}
