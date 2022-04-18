package com.blue.verify.component.verify;

import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.api.model.VerifyParam;
import com.blue.verify.component.verify.inter.VerifyHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.getBusinessTypeByIdentity;
import static com.blue.base.common.base.ConstantProcessor.getVerifyTypeByIdentity;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;

/**
 * verify processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class VerifyProcessor implements ApplicationListener<ContextRefreshedEvent> {

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

    /**
     * generate for api
     *
     * @param verifyType
     * @param businessType
     * @param destination
     * @return
     */
    public Mono<String> handle(VerifyType verifyType, BusinessType businessType, String destination) {
        if (isNotNull(verifyType) && isNotNull(businessType))
            return ofNullable(verifyHandlers.get(verifyType))
                    .map(h -> h.handle(businessType, destination))
                    .orElseThrow(() -> new BlueException(INVALID_PARAM));

        return error(() -> new BlueException(INVALID_PARAM));
    }

    /**
     * generate for endpoint
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(VerifyParam.class)
                .switchIfEmpty(
                        error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(vp ->
                        ofNullable(vp.getVerifyType())
                                .map(verifyType -> verifyHandlers.get(getVerifyTypeByIdentity(verifyType)))
                                .map(h -> h.handle(getBusinessTypeByIdentity(vp.getBusinessType()), vp.getDestination(), serverRequest))
                                .orElseThrow(() -> new BlueException(INVALID_PARAM)));
    }

    /**
     * validate verify
     *
     * @param verifyType
     * @param businessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    public Mono<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable) {
        if (isNotNull(verifyType) && isNotNull(businessType) && isNotBlank(key) && isNotBlank(verify))
            return ofNullable(verifyHandlers.get(verifyType))
                    .map(h -> h.validate(businessType, key, verify, repeatable))
                    .orElseThrow(() -> new BlueException(INVALID_PARAM));

        return error(() -> new BlueException(INVALID_PARAM));
    }

}
