package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SpecialAccess;
import com.blue.basic.constant.common.SpecialSession;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.Session;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.lake.config.deploy.CaffeineDeploy;
import com.blue.lake.remote.consumer.RpcAuthServiceConsumer;
import com.blue.lake.service.inter.AuthService;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

/**
 * auth service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = getLogger(AuthServiceImpl.class);

    private RpcAuthServiceConsumer rpcAuthServiceConsumer;

    public AuthServiceImpl(RpcAuthServiceConsumer rpcAuthServiceConsumer, ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;

        accessInfoCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getAccessMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService));

        sessionInfoCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getSessionMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService));
    }

    private AsyncCache<String, Access> accessInfoCache;

    private AsyncCache<String, Session> sessionInfoCache;

    private final BiFunction<String, Executor, CompletableFuture<Access>> ACCESS_INFO_REMOTE_GETTER = (authentication, executor) ->
            rpcAuthServiceConsumer.parseAccess(authentication)
                    .switchIfEmpty(defer(() -> just(SpecialAccess.VISITOR.access)))
                    .onErrorResume(t -> {
                        LOGGER.error("ACCESS_INFO_REMOTE_GETTER failed, t = {}", t.getMessage());
                        return just(SpecialAccess.VISITOR.access);
                    }).toFuture();

    private final BiFunction<String, Executor, CompletableFuture<Session>> SESSION_INFO_REMOTE_GETTER = (authentication, executor) ->
            rpcAuthServiceConsumer.parseSession(authentication)
                    .switchIfEmpty(defer(() -> just(SpecialSession.VISITOR.session)))
                    .onErrorResume(t -> {
                        LOGGER.error("SESSION_INFO_REMOTE_GETTER failed, t = {}", t.getMessage());
                        return just(SpecialSession.VISITOR.session);
                    }).toFuture();

    private final Function<String, CompletableFuture<Access>> ACCESS_INFO_GETTER = authentication ->
            ofNullable(authentication).filter(BlueChecker::isNotBlank)
                    .map(a -> accessInfoCache.get(a, ACCESS_INFO_REMOTE_GETTER))
                    .orElseGet(() -> supplyAsync(() -> SpecialAccess.VISITOR.access));

    private final Function<String, CompletableFuture<Session>> SESSION_INFO_GETTER = authentication ->
            ofNullable(authentication).filter(BlueChecker::isNotBlank)
                    .map(a -> sessionInfoCache.get(a, SESSION_INFO_REMOTE_GETTER))
                    .orElseGet(() -> supplyAsync(() -> SpecialSession.VISITOR.session));

    @Override
    public Mono<Access> parseAccess(String authentication) {
        return fromFuture(ACCESS_INFO_GETTER.apply(authentication));
    }

    @Override
    public Mono<Session> parseSession(String authentication) {
        return fromFuture(SESSION_INFO_GETTER.apply(authentication));
    }

}
