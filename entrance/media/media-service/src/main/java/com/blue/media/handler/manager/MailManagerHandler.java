package com.blue.media.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.mail.common.MailReader;
import com.blue.mail.common.MailSender;
import jakarta.mail.Message;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.media.MailHeader.LIST_UNSUBSCRIBE;
import static com.blue.mail.common.MailReader.parseMessage;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2022/1/5
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode", "AliControlFlowStatementWithoutBraces"})
@Component
public class MailManagerHandler {

    private static final Logger LOGGER = getLogger(MailManagerHandler.class);

    private final MailSender mailSender;

    private final MailReader mailReader;

    public MailManagerHandler(MailSender mailSender, MailReader mailReader) {
        this.mailSender = mailSender;
        this.mailReader = mailReader;
    }

    private static final String FROM = "yunfei0221@outlook.com";

    private static final List<String> RECEIVERS = Stream.of(
            "liuyunfei19890221@gmail.com",
            "liuyunfei198902210221@163.com",
            "liuyunfei19890221@163.com",
            "yunfei.liu@dreamisland.ai"
    ).collect(toList());


    /**
     * test
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> testSend(ServerRequest serverRequest) {
        testSend();
        return just(true)
                .flatMap(t ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }

    /**
     * test
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> testRead(ServerRequest serverRequest) {
        Message[] messages = mailReader.getMessages();
        for (Message msg : messages)
            parseMessage(msg);

        return just(true)
                .flatMap(t ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }


    //=============================================================================================================================

    private void testSend() {
        Message message = mailSender.initMessage();

        try {
            message.setFrom(new InternetAddress(FROM));

            InternetAddress[] tos = RECEIVERS.stream().map(r -> {
                try {
                    return new InternetAddress(r);
                } catch (AddressException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }).toArray(InternetAddress[]::new);
            message.setRecipients(Message.RecipientType.TO, tos);

            message.addHeader(LIST_UNSUBSCRIBE.name, "https://www.baidu.com/");
            message.setSubject("hello new world");

            message.setText("Please view this email in a modern email client!");

            long start = System.currentTimeMillis();

            CompletableFuture<Void> future = mailSender.sendMessage(message)
                    .thenAcceptAsync(v -> {
                        System.err.println("SEND SUCCESS!!!");
                        System.err.println("DURATION MILLIS: " + (System.currentTimeMillis() - start));
                    })
                    .exceptionally(t -> {
                        LOGGER.error("SEND FAILED!!!");
                        LOGGER.error("t = {}", t);
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
