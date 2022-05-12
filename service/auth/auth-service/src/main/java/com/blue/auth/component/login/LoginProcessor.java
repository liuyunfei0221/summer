package com.blue.auth.component.login;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.model.exps.BlueException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.auth.constant.LoginAttribute.VERIFICATION_CODE;
import static com.blue.auth.constant.LoginAttribute.VERIFICATION_KEY;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.verify.BusinessType.CREDENTIAL_ACCESS_LOGIN;
import static com.blue.base.constant.verify.VerifyType.IMAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static reactor.core.publisher.Mono.*;

/**
 * login processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class LoginProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    public LoginProcessor(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
    }

    private static final Set<String> ALLOW_ACCESS_LTS = Stream.of(CredentialType.values())
            .filter(lt -> lt.allowAccess).map(lt -> lt.identity).collect(toSet());

    private final BiFunction<String, String, Mono<Boolean>> IMAGE_VERIFY_VALIDATOR = (key, verify) ->
            rpcVerifyHandleServiceConsumer.validate(IMAGE, CREDENTIAL_ACCESS_LOGIN, key, verify, false);

    private final BiFunction<String, LoginParam, Mono<Boolean>> ACCESS_VERIFY_VALIDATOR = (credentialType, loginParam) -> {
        if (isBlank(credentialType))
            return error(() -> new BlueException(INVALID_PARAM));

        if (!ALLOW_ACCESS_LTS.contains(credentialType))
            return just(true);

        String verifyKey = loginParam.getData(VERIFICATION_KEY.key);
        String verify = loginParam.getData(VERIFICATION_CODE.key);
        if (isBlank(verifyKey) || isBlank(verify))
            return error(() -> new BlueException(INVALID_PARAM));

        return IMAGE_VERIFY_VALIDATOR.apply(verifyKey, verify);
    };

    /**
     * credential type -> login handler
     */
    private Map<String, LoginHandler> loginHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, LoginHandler> beansOfType = applicationContext.getBeansOfType(LoginHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "loginHandlers is empty");

        loginHandlers = beansOfType.values().stream().collect(toMap(lh -> lh.targetType().identity, lh -> lh, (a, b) -> a));
    }

    private final Function<ServerRequest, Mono<ServerResponse>> loginHandler = serverRequest ->
            serverRequest.bodyToMono(LoginParam.class)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                    .flatMap(lp -> {
                        String credentialType = lp.getCredentialType();
                        return ACCESS_VERIFY_VALIDATOR.apply(credentialType, lp)
                                .flatMap(validate ->
                                        validate ?
                                                ofNullable(loginHandlers.get(credentialType))
                                                        .map(h -> h.login(lp, serverRequest))
                                                        .orElseThrow(() -> new BlueException(INVALID_PARAM))
                                                :
                                                error(() -> new BlueException(VERIFY_IS_INVALID)));
                    });

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return loginHandler.apply(serverRequest);
    }

}
