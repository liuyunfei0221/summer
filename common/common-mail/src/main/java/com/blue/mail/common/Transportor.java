package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import reactor.util.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Thread.onSpinWait;
import static reactor.util.Loggers.getLogger;

/**
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class Transportor {

    private static final Logger LOGGER = getLogger(Transportor.class);

    private Session session;

    private volatile Transport transport;

    private final AtomicBoolean TRANSPORT_CONTROL = new AtomicBoolean(true);

    public Transportor(Session session) {
        this.session = session;
        TRANSPORT_CONNECTOR.accept(this.session);
    }

    private final Consumer<Session> TRANSPORT_CONNECTOR = sn -> {
        if (TRANSPORT_CONTROL.compareAndSet(true, false)) {
            try {
                Transport transport = session.getTransport();
                transport.connect();

                this.transport = transport;
                TRANSPORT_CONTROL.compareAndSet(false, true);
            } catch (MessagingException e) {
                LOGGER.error("transport connect failed, e = {}", e);
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "transport connect failed, e = " + e);
            }
        } else {
            while (!TRANSPORT_CONTROL.get())
                onSpinWait();
        }
    };

    private final Consumer<Message> MESSAGE_SENDER = message -> {
        try {
            this.transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            TRANSPORT_CONNECTOR.accept(this.session);
            try {
                this.transport.sendMessage(message, message.getAllRecipients());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public void sendMessage(Message message) {
        MESSAGE_SENDER.accept(message);
    }

}
