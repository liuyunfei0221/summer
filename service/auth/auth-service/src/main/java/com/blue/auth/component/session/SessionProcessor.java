package com.blue.auth.component.session;

import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.auth.service.inter.AuthService;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
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
import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.verify.VerifyBusinessType.CREDENTIAL_ACCESS_LOGIN;
import static com.blue.basic.constant.verify.VerifyType.IMAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * session processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class SessionProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final AuthService authService;

    public SessionProcessor(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, AuthService authService) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.authService = authService;
    }

    private static final Set<String> ALLOW_TURING_LTS = Stream.of(CredentialType.values())
            .filter(lt -> lt.allowTuring).map(lt -> lt.identity).collect(toSet());

    private final BiFunction<String, String, Mono<Boolean>> IMAGE_VERIFY_VALIDATOR = (key, verify) ->
            rpcVerifyHandleServiceConsumer.validate(IMAGE, CREDENTIAL_ACCESS_LOGIN, key, verify, false);

    private final BiFunction<String, LoginParam, Mono<Boolean>> ACCESS_VERIFY_VALIDATOR = (credentialType, loginParam) -> {
        if (isBlank(credentialType))
            return error(() -> new BlueException(INVALID_PARAM));

        if (!ALLOW_TURING_LTS.contains(credentialType))
            return just(true);

        String verifyKey = loginParam.getData(VERIFICATION_KEY.key);
        String verify = loginParam.getData(VERIFICATION_CODE.key);
        if (isBlank(verifyKey) || isBlank(verify))
            return error(() -> new BlueException(INVALID_PARAM));

        return IMAGE_VERIFY_VALIDATOR.apply(verifyKey, verify);
    };

    /**
     * credential type -> session handler
     */
    private Map<String, SessionHandler> sessionHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, SessionHandler> beansOfType = applicationContext.getBeansOfType(SessionHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("sessionHandlers is empty");

        sessionHandlers = beansOfType.values().stream().collect(toMap(lh -> lh.targetType().identity, lh -> lh, (a, b) -> a));
    }

    private final Function<ServerRequest, Mono<ServerResponse>> sessionHandler = serverRequest ->
            serverRequest.bodyToMono(LoginParam.class)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                    .flatMap(lp -> {
                        String credentialType = lp.getCredentialType();
                        return ACCESS_VERIFY_VALIDATOR.apply(credentialType, lp)
                                .flatMap(validate ->
                                        validate ?
                                                ofNullable(sessionHandlers.get(credentialType))
                                                        .map(h -> h.login(lp, serverRequest))
                                                        .orElseThrow(() -> new BlueException(INVALID_PARAM))
                                                :
                                                error(() -> new BlueException(VERIFY_IS_INVALID)));
                    });

    /**
     * session
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertSession(ServerRequest serverRequest) {
        return sessionHandler.apply(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> deleteSession(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authService.invalidateAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_VALUE.value)
                                                .body(
                                                        success(serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> deleteSessions(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authService.invalidateAuthByMemberId(acc.getId())
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_VALUE.value)
                                                .body(success(serverRequest)
                                                        , BlueResponse.class)));
    }

}
