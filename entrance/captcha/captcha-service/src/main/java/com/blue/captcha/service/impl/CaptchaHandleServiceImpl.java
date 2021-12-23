package com.blue.captcha.service.impl;

import com.blue.base.constant.captcha.CaptchaType;
import com.blue.base.model.exps.BlueException;
import com.blue.captcha.api.model.CaptchaParam;
import com.blue.captcha.component.captcha.inter.CaptchaHandler;
import com.blue.captcha.service.inter.CaptchaHandleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.INVALID_PARAM;
import static com.blue.base.constant.captcha.CaptchaType.IMAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;

/**
 * captcha service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CaptchaHandleServiceImpl implements CaptchaHandleService, ApplicationListener<ContextRefreshedEvent> {

    /**
     * captcha type -> captcha handler
     */
    private Map<CaptchaType, CaptchaHandler> captchaHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, CaptchaHandler> beansOfType = applicationContext.getBeansOfType(CaptchaHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "captchaHandlers is empty");

        captchaHandlers = beansOfType.values().stream()
                .collect(toMap(CaptchaHandler::targetType, ch -> ch, (a, b) -> a));
    }

    private static final CaptchaParam DEFAULT_PARAM = new CaptchaParam(IMAGE, "");

    private final Function<ServerRequest, Mono<ServerResponse>> captchaHandler = serverRequest ->
            serverRequest.bodyToMono(CaptchaParam.class)
                    .switchIfEmpty(just(DEFAULT_PARAM))
                    .flatMap(cp ->
                            ofNullable(cp.getCaptchaType())
                                    .map(captchaHandlers::get)
                                    .map(h -> h.handle(cp.getDestination()))
                                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));

    /**
     * generate captcha
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return captchaHandler.apply(serverRequest);
    }

}
