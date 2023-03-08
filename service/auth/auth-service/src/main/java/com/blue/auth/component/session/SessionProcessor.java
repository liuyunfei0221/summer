package com.blue.auth.component.session;

import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.config.deploy.SessionDeploy;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.auth.service.inter.AuthService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.TuringData;
import com.blue.basic.model.exps.BlueException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.getIpReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.turing.TuringDataGetter.getTuringDataReact;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.ALLOW_TURING_CREDENTIAL_TYPE_RATE_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
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
@Order(HIGHEST_PRECEDENCE)
public class SessionProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final AuthService authService;

    public SessionProcessor(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, AuthService authService, SessionDeploy sessionDeploy) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.authService = authService;

        Integer allow = sessionDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long intervalMillis = sessionDeploy.getIntervalMillis();
        if (isNull(intervalMillis) || intervalMillis < 1L)
            throw new RuntimeException("intervalMillis can't be null or less than 1");

        this.allow = allow;
        this.intervalMillis = intervalMillis;
    }

    private int allow;
    private long intervalMillis;

    private static final UnaryOperator<String> TURING_LIMIT_IDENTITY_WRAPPER = key -> {
        if (isBlank(key))
            throw new BlueException(INVALID_PARAM);

        return ALLOW_TURING_CREDENTIAL_TYPE_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private final BiFunction<String, ServerRequest, Mono<Boolean>> TURING_TEST_VALIDATOR = (credentialType, serverRequest) ->
            (isNotBlank(credentialType) && isNotNull(serverRequest)) ?
                    zip(
                            getIpReact(serverRequest),
                            getTuringDataReact(serverRequest)
                    ).flatMap(tuple2 -> {
                        TuringData turingData = tuple2.getT2();
                        return rpcVerifyHandleServiceConsumer.turingValidate(
                                TURING_LIMIT_IDENTITY_WRAPPER.apply(tuple2.getT1()), allow, intervalMillis, turingData.getKey(), turingData.getVerify());
                    })
                    :
                    error(() -> new BlueException(INVALID_PARAM));


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
                        return TURING_TEST_VALIDATOR.apply(credentialType, serverRequest)
                                .flatMap(validate ->
                                        validate ?
                                                ofNullable(sessionHandlers.get(credentialType))
                                                        .map(h -> h.login(lp, serverRequest))
                                                        .orElseThrow(() -> new BlueException(INVALID_PARAM))
                                                :
                                                error(() -> new BlueException(NEED_TURING_TEST)));
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
