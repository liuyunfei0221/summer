//package com.blue.verify.component.verify.impl;
//
//import com.blue.base.constant.base.RandomType;
//import com.blue.base.constant.verify.VerifyType;
//import com.blue.verify.api.model.VerifyPair;
//import com.blue.verify.component.verify.inter.VerifyHandler;
//import com.blue.verify.config.deploy.SmsVerifyDeploy;
//import com.blue.verify.service.inter.SmsService;
//import com.blue.verify.service.inter.VerifyService;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.util.FastByteArrayOutputStream;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//import reactor.util.Logger;
//
//import java.time.Duration;
//import java.util.concurrent.ExecutorService;
//
//import static com.blue.base.common.base.BlueCheck.isBlank;
//import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
//import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
//import static com.blue.base.constant.verify.VerifyType.IMAGE;
//import static com.blue.base.constant.verify.VerifyType.SMS;
//import static java.time.temporal.ChronoUnit.MILLIS;
//import static org.springframework.http.MediaType.IMAGE_PNG;
//import static org.springframework.web.reactive.function.BodyInserters.fromResource;
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//import static reactor.core.publisher.Mono.using;
//import static reactor.util.Loggers.getLogger;
//
///**
// * sms verify handler
// *
// * @author liuyunfei
// * @date 2021/12/23
// * @apiNote
// */
//@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
//@Component
//@Order(Ordered.LOWEST_PRECEDENCE - 1)
//public class SmsVerifyHandler implements VerifyHandler {
//
//    private static final Logger LOGGER = getLogger(SmsVerifyHandler.class);
//
//    private final SmsService smsService;
//
//    private final VerifyService verifyService;
//
//    private ExecutorService executorService;
//
//    private final int VERIFY_LEN;
//    private final Duration DEFAULT_DURATION;
//
//    public SmsVerifyHandler(SmsService smsService, VerifyService verifyService, ExecutorService executorService, SmsVerifyDeploy smsVerifyDeploy) {
//        this.smsService = smsService;
//        this.verifyService = verifyService;
//        this.executorService = executorService;
//
//        Integer verifyLength = smsVerifyDeploy.getVerifyLength();
//        if (verifyLength == null || verifyLength < 1)
//            throw new RuntimeException("verifyLength can't be null or less than 1");
//
//        Integer expireMillis = smsVerifyDeploy.getExpireMillis();
//        if (expireMillis == null || expireMillis < 1)
//            throw new RuntimeException("expireMillis can't be null or less than 1");
//
//        this.VERIFY_LEN = verifyLength;
//        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
//    }
//
//
//    /**
//     * generate an image verify
//     *
//     * @param destination
//     * @return
//     */
//    @Override
//    public Mono<ServerResponse> handle(String destination) {
//
//        //ratelimiter
//
//        String phone = destination;
//
//        Mono<VerifyPair> generate = verifyService.generate(SMS, phone, VERIFY_LEN, DEFAULT_DURATION);
//
//
//        return verifyService.generate(IMAGE, generateRandom(KEY_RANDOM_TYPE, KEY_LEN), VERIFY_LEN, DEFAULT_DURATION)
//                .flatMap(cp -> {
//                    String key = cp.getKey();
//                    String verify = cp.getVerify();
//
//                    LOGGER.info("Mono<ServerResponse> handle(String destination), key = {}, verify = {}", key, verify);
//
//                    return using(FastByteArrayOutputStream::new,
//                            outputStream -> IMAGE_WRITER.apply(verify, outputStream)
//                            , STREAM_CLOSER,
//                            true)
//                            .flatMap(resource ->
//                                    ok().contentType(IMAGE_PNG)
//                                            .header(VERIFY_KEY.name, key)
//                                            .body(fromResource(resource))
//                            );
//                });
//    }
//
//    @Override
//    public VerifyType targetType() {
//        return SMS;
//    }
//
//}
