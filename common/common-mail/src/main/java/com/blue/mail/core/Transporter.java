package com.blue.mail.core;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.lang.Thread.onSpinWait;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Transport with auto connect
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "AlibabaAvoidManuallyCreateThread", "unused", "AlibabaLowerCamelCaseVariableNaming"})
final class Transporter {

    private static final Logger LOGGER = getLogger(Transporter.class);

    private Session session;

    private volatile Transport transport;

    private final AtomicBoolean TRANSPORT_CONTROL = new AtomicBoolean(false);

    private String FROM_ADDRESS;

    public Transporter(Session session, String fromAddress) {
        this.session = session;
        this.FROM_ADDRESS = fromAddress;
    }

    private final Consumer<Session> TRANSPORT_CONNECTOR = sn -> {
        try {
            Transport transport = session.getTransport();
            transport.connect();

            this.transport = transport;
            TRANSPORT_CONTROL.compareAndSet(false, true);

            LOGGER.warn("Transporter connect");
        } catch (MessagingException e) {
            LOGGER.error("Transport connect failed, e = {}", e.getMessage());
            throw new RuntimeException("Transport connect failed, e = " + e);
        }
    };

    private final Consumer<Message> MESSAGE_COMPLETER = message -> {
        try {
            message.setFrom(new InternetAddress(FROM_ADDRESS));
        } catch (MessagingException e) {
            LOGGER.error("MESSAGE_COMPLETER failed, e = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    };

    private void reConnect() {
        if (TRANSPORT_CONTROL.compareAndSet(true, false)) {
            try {
                TRANSPORT_CONNECTOR.accept(this.session);
                TRANSPORT_CONTROL.compareAndSet(false, true);
            } catch (Exception e) {
                throw new RuntimeException("Transport connect failed, e = " + e);
            } finally {
                TRANSPORT_CONTROL.set(true);
            }
        } else {
            while (!TRANSPORT_CONTROL.get())
                onSpinWait();
        }
    }

    private void sendMsg(Message message) {
        while (!TRANSPORT_CONTROL.get())
            onSpinWait();

        try {
            this.transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            reConnect();
            try {
                this.transport.sendMessage(message, message.getAllRecipients());
            } catch (MessagingException ex) {
                throw new RuntimeException("sendMsg, failed, e = " + e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        TRANSPORT_CONNECTOR.accept(this.session);
        TRANSPORT_CONTROL.set(true);
    }

    public MimeMessage initMessage() {
        return new MimeMessage(session);
    }

    public void sendMessage(Message message) {
        MESSAGE_COMPLETER.accept(message);
        sendMsg(message);
    }

    public String from() {
        return FROM_ADDRESS;
    }

}
