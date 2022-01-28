package com.blue.mail.core;

import com.blue.base.model.exps.BlueException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import reactor.util.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Thread.onSpinWait;
import static reactor.util.Loggers.getLogger;

/**
 * Transport with auto connect
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "AlibabaAvoidManuallyCreateThread", "unused", "AlibabaLowerCamelCaseVariableNaming"})
final class Transporter {

    private static final Logger LOGGER = getLogger(Transporter.class);

    private Session session;

    private volatile Transport transport;

    private final AtomicBoolean TRANSPORT_CONTROL = new AtomicBoolean(false);

    public Transporter(Session session) {
        this.session = session;
    }

    private final Consumer<Session> TRANSPORT_CONNECTOR = sn -> {
        try {
            Transport transport = session.getTransport();
            transport.connect();

            this.transport = transport;
            TRANSPORT_CONTROL.compareAndSet(false, true);

            LOGGER.warn("Transporter connect");
        } catch (MessagingException e) {
            LOGGER.error("transport connect failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "transport connect failed, e = " + e);
        }
    };

    private void sendMsg(Message message) {
        while (!TRANSPORT_CONTROL.get())
            onSpinWait();

        try {
            this.transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            sendMsgWithReConnect(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMsgWithReConnect(Message message) {
        if (TRANSPORT_CONTROL.compareAndSet(true, false)) {
            try {
                TRANSPORT_CONNECTOR.accept(this.session);
                TRANSPORT_CONTROL.compareAndSet(false, true);
            } catch (Exception e) {
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "transport connect failed, e = " + e);
            } finally {
                TRANSPORT_CONTROL.set(true);
            }
        } else {
            while (!TRANSPORT_CONTROL.get())
                onSpinWait();
        }

        sendMsg(message);
    }

    public void init() {
        TRANSPORT_CONNECTOR.accept(this.session);
        TRANSPORT_CONTROL.set(true);
    }

    public MimeMessage initMessage() {
        return new MimeMessage(session);
    }

    public void sendMessage(Message message) {
        sendMsg(message);
    }

}
