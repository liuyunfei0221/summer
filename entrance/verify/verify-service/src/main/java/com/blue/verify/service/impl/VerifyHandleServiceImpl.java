package com.blue.verify.service.impl;

import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.api.model.VerifyParam;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.service.inter.VerifyHandleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueCheck.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.INVALID_PARAM;
import static com.blue.base.constant.verify.VerifyType.IMAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;

/**
 * verify service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class VerifyHandleServiceImpl implements VerifyHandleService, ApplicationListener<ContextRefreshedEvent> {

    /**
     * verify type -> verify handler
     */
    private Map<VerifyType, VerifyHandler> verifyHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, VerifyHandler> beansOfType = applicationContext.getBeansOfType(VerifyHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "verifyHandlers is empty");

        verifyHandlers = beansOfType.values().stream()
                .collect(toMap(VerifyHandler::targetType, vh -> vh, (a, b) -> a));
    }

    private static final VerifyParam DEFAULT_PARAM = new VerifyParam(IMAGE, "");

    private final Function<ServerRequest, Mono<ServerResponse>> verifyHandler = serverRequest ->
            serverRequest.bodyToMono(VerifyParam.class)
                    .switchIfEmpty(just(DEFAULT_PARAM))
                    .flatMap(cp ->
                            ofNullable(cp.getVerifyType())
                                    .map(verifyHandlers::get)
                                    .map(h -> h.handle(cp.getDestination()))
                                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));

    /**
     * generate verify
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return verifyHandler.apply(serverRequest);
    }

}
