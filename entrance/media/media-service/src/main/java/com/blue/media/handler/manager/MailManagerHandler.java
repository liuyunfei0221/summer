package com.blue.media.handler.manager;

import com.blue.base.common.base.FileProcessor;
import com.blue.base.model.base.BlueResponse;
import com.blue.mail.common.MailSender;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.media.MailHeader.LIST_UNSUBSCRIBE;
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
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
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

        mailTest();

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

        mailTest();

        return just(true)
                .flatMap(t ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }



    private void mailTest() {
        EmailPopulatingBuilder builder = EmailBuilder.startingBlank()
                .from("blue", FROM)
                .toWithFixedName("darkBlue", RECEIVERS)
                .withHeader(LIST_UNSUBSCRIBE.name, "https://www.baidu.com/")

                .withDispositionNotificationTo(FROM)
                .withReturnReceiptTo(FROM)

                .withSubject("hello world")
                .withHTMLText("Please view this email in a modern email client!");

        mailSender.signWithDomainKey(builder);

        CompletableFuture<Void> future = mailSender.sendMail(builder.buildEmail())
                .thenAcceptAsync(v -> System.err.println("SEND SUCCESS!!!"))
                .exceptionally(t -> {
                    LOGGER.error("SEND FAILED!!!");
                    LOGGER.error("t = {}", t);
                    return null;
                });
    }

    private void mediaMailTest() {
        String htmlText = "<img src='cid:logo.jpg'><b>We should meet up!</b><img src='logo.jpg'>";

        EmailPopulatingBuilder builder = EmailBuilder.startingBlank()
                .from("blue", FROM)
                .toWithFixedName("darkBlue", RECEIVERS)
                .withHeader(LIST_UNSUBSCRIBE.name, "https://www.baidu.com/")
                .withHeader("Content-ID", "<logo.jpg>")
                .withSubject("hello world")
                .withHTMLText(htmlText);

//        File file = FileProcessor.getFile("E:\\tempFile\\source\\walls\\a_beautiful_view_of_colorful_autumn_trees-wallpaper-2560x1440.jpg");
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            builder.withAttachment("img.jpg", fileInputStream.readAllBytes(), MediaType.IMAGE_JPEG_VALUE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        File logoFile = FileProcessor.getFile("E:\\tempFile\\source\\favicon.jpg");
        try (FileInputStream fileInputStream = new FileInputStream(logoFile)) {
            builder.withAttachment("logo.jpg", fileInputStream.readAllBytes(), MediaType.IMAGE_JPEG_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mailSender.signWithDomainKey(builder);

        CompletableFuture<Void> future = mailSender.sendMail(builder.buildEmail())
                .thenAcceptAsync(v ->
                        System.err.println("SEND SUCCESS!!!"))
                .exceptionally(t -> {
                    LOGGER.error("SEND FAILED!!!");
                    LOGGER.error("t = {}", t);
                    return null;
                });
    }

}
