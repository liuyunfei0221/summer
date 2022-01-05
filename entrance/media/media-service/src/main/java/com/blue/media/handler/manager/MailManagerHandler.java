package com.blue.media.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.mail.common.MailSender;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
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
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class MailManagerHandler {

    private static final Logger LOGGER = getLogger(MailManagerHandler.class);

    private final MailSender mailSender;

    public MailManagerHandler(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final String FROM = "yunfei0221@outlook.com";

    private static final List<String> RECEIVERS = Stream.of(
            "liuyunfei19890221@gmail.com",
            "liuyunfei19890221@163.com"
    ).collect(toList());


    /**
     * test
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> testMail(ServerRequest serverRequest) {
        Email email = EmailBuilder.startingBlank()
                .from(FROM)
                .toMultiple(RECEIVERS)
                .withSubject("hello world")
                .withPlainText("Please view this email in a modern email client!")
                .buildEmail();

        CompletableFuture<Void> future = mailSender.sendMail(email)
                .thenAcceptAsync(v -> System.err.println("SEND SUCCESS!!!"))
                .exceptionally(t -> {
                    LOGGER.error("SEND FAILED!!!");
                    return null;
                });

        return just(true)
                .flatMap(t ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }

}
