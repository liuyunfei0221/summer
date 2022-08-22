package com.blue.verify.component.verify;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.exps.BlueException;
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
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.getVerifyBusinessTypeByIdentity;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * verify processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class VerifyProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Map<String, Set<String>> BT_ALLOWED_VTS = Stream.of(VerifyBusinessType.values())
            .collect(toMap(bt -> bt.identity, bt -> bt.allowedVerifyTypes.stream().map(vt -> vt.identity).collect(Collectors.toSet())));

    private static final BiConsumer<String, String> ALLOWED_ASSERTER = (businessType, verifyType) -> {
        if (isBlank(businessType) || isBlank(verifyType))
            throw new BlueException(INVALID_PARAM);

        Set<String> verifyTypes = BT_ALLOWED_VTS.get(businessType);
        if (isEmpty(verifyTypes) || !verifyTypes.contains(verifyType))
            throw new BlueException(UNSUPPORTED_OPERATE);
    };

    private static final int VFK_LEN_MIN = (int) VF_K_LEN_MIN.value,
            VFK_LEN_MAX = (int) VF_K_LEN_MAX.value;

    private static final int VFV_LEN_MIN = (int) VF_V_LEN_MIN.value,
            VFV_LEN_MAX = (int) VF_V_LEN_MAX.value;

    private static final Consumer<String> KEY_ASSERTER = key -> {
        int len;
        if (isBlank(key) || (len = key.length()) < VFK_LEN_MIN || len > VFK_LEN_MAX)
            throw new BlueException(INVALID_PARAM);
    };

    private static final Consumer<String> VERIFY_ASSERTER = verify -> {
        int len;
        if (isBlank(verify) || (len = verify.length()) < VFV_LEN_MIN || len > VFV_LEN_MAX)
            throw new BlueException(INVALID_PARAM);
    };

    /**
     * verify type -> verify handler
     */
    private Map<String, VerifyHandler> verifyHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, VerifyHandler> beansOfType = applicationContext.getBeansOfType(VerifyHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("verifyHandlers is empty");

        verifyHandlers = beansOfType.values().stream()
                .collect(toMap(vh -> vh.targetType().identity, vh -> vh, (a, b) -> a));
    }

    /**
     * generate for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @return
     */
    public Mono<String> handle(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination) {
        if (isNotNull(verifyType) && isNotNull(verifyBusinessType))
            return ofNullable(verifyHandlers.get(verifyType.identity))
                    .map(h -> {
                        ALLOWED_ASSERTER.accept(verifyBusinessType.identity, verifyType.identity);
                        return h.handle(verifyBusinessType, destination);
                    })
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
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(vp -> {
                    String verifyType = vp.getVerifyType();
                    String businessType = vp.getBusinessType();
                    ALLOWED_ASSERTER.accept(businessType, verifyType);

                    String destination = vp.getDestination();
                    if (isNotBlank(destination) && destination.length() > VFK_LEN_MAX)
                        return error(() -> new BlueException(INVALID_PARAM));

                    return ofNullable(verifyHandlers.get(verifyType))
                            .map(h -> h.handle(getVerifyBusinessTypeByIdentity(businessType), destination, serverRequest))
                            .orElseThrow(() -> new BlueException(INVALID_PARAM));
                });

    }

    /**
     * validate verify
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    public Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        ALLOWED_ASSERTER.accept(verifyBusinessType.identity, verifyType.identity);
        KEY_ASSERTER.accept(key);
        VERIFY_ASSERTER.accept(verify);

        return ofNullable(verifyHandlers.get(verifyType.identity))
                .map(h -> h.validate(verifyBusinessType, key, verify, repeatable))
                .orElseThrow(() -> new BlueException(INVALID_PARAM));
    }

}
