package com.blue.mail.component;

import com.blue.basic.common.base.BlueChecker;
import com.blue.mail.api.conf.MailReaderConf;
import jakarta.mail.*;
import jakarta.mail.event.ConnectionListener;
import jakarta.mail.event.FolderListener;
import jakarta.mail.event.MessageChangedListener;
import jakarta.mail.event.MessageCountListener;
import jakarta.mail.search.SearchTerm;
import reactor.util.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.OriginalThrowableGetter.getOriginalThrowable;
import static com.blue.mail.processor.ReaderCommonProcessor.*;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * mail reader
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "unused", "SpellCheckingInspection", "DuplicatedCode"})
public final class MailReader {

    private static final Logger LOGGER = getLogger(MailReader.class);

    private Session session;

    private String folderName;

    private Folder folder;

    private Set<String> throwableForRetry;

    private long maxWaitingMillisForRefresh;

    public MailReader(MailReaderConf conf) {
        this.session = generateSession(conf);
        this.folderName = conf.getFolderName();

        this.folder = openFolder(generateStore(session), folderName);

        this.throwableForRetry = ofNullable(conf.getThrowableForRetry())
                .filter(BlueChecker::isNotEmpty).map(HashSet::new).orElseGet(HashSet::new);
        this.maxWaitingMillisForRefresh = conf.getMaxWaitingMillisForRefresh();
    }

    private volatile boolean folderComplete = true;

    private final Supplier<Folder> FOLDER_SUP = () -> {
        if (folderComplete)
            return this.folder;

        long start = currentTimeMillis();
        while (!folderComplete) {
            if (currentTimeMillis() - start > maxWaitingMillisForRefresh)
                throw new RuntimeException("FOLDER_SUP failed");
            onSpinWait();
        }

        return this.folder;
    };

    private final Supplier<Folder> FOLDER_SUP_WITH_REFRESHER = () -> {
        if (folderComplete)
            synchronized (this) {
                if (folderComplete) {
                    this.folderComplete = false;
                    this.folder = openFolder(generateStore(session), folderName);
                    this.folderComplete = true;
                }
            }

        return FOLDER_SUP.get();
    };

    private final Predicate<Throwable> REFRESH_PREDICATE = throwable ->
            isNotNull(throwable) && throwableForRetry.contains(getOriginalThrowable(throwable).getClass().getName());


    public void parseMessage(Message message) {
        MessageParser.parseMessage(message);
    }

    public int getMessageCount() {
        try {
            return FOLDER_SUP.get().getMessageCount();
        } catch (Throwable throwable) {
            LOGGER.error(" throwable = {}", throwable);
            if (REFRESH_PREDICATE.test(throwable)) {
                try {
                    return FOLDER_SUP_WITH_REFRESHER.get().getMessageCount();
                } catch (MessagingException ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, throwable = {}", throwable);
        }
    }

    public int getNewMessageCount() {
        try {
            return FOLDER_SUP.get().getNewMessageCount();
        } catch (Exception e) {
            if (REFRESH_PREDICATE.test(e)) {
                try {
                    return FOLDER_SUP_WITH_REFRESHER.get().getNewMessageCount();
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getUnreadMessageCount();
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getUnreadMessageCount();
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getMessage(index);
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getMessages(start, end);
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getMessages(msgnums);
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
                    return FOLDER_SUP_WITH_REFRESHER.get().getMessages();
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
                    FOLDER_SUP_WITH_REFRESHER.get().appendMessages(messages);
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
                    FOLDER_SUP_WITH_REFRESHER.get().fetch(msgs, fp);
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
                    FOLDER_SUP_WITH_REFRESHER.get().setFlags(msgs, flag, value);
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
                    FOLDER_SUP_WITH_REFRESHER.get().setFlags(start, end, flag, value);
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
                    FOLDER_SUP_WITH_REFRESHER.get().setFlags(msgnums, flag, value);
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
                    FOLDER_SUP_WITH_REFRESHER.get().copyMessages(msgs, folder);
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
                    return FOLDER_SUP_WITH_REFRESHER.get().expunge();
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
                    return FOLDER_SUP_WITH_REFRESHER.get().search(term);
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
                    return FOLDER_SUP_WITH_REFRESHER.get().search(term, msgs);
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
                    FOLDER_SUP_WITH_REFRESHER.get().addConnectionListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().removeConnectionListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().addFolderListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().removeFolderListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().addMessageCountListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().removeMessageCountListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().addMessageChangedListener(l);
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
                    FOLDER_SUP_WITH_REFRESHER.get().removeMessageChangedListener(l);
                } catch (Exception ex) {
                    throw new RuntimeException("handle failed, e = {}", ex);
                }
            }
            throw new RuntimeException("handle failed, e = {}", e);
        }
    }

}
