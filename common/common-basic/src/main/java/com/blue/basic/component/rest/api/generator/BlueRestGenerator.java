package com.blue.basic.component.rest.api.generator;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.component.rest.api.conf.RestConf;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import reactor.util.Logger;

import java.time.Duration;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.component.rest.constant.DefaultConf.*;
import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.channel.ChannelOption.TCP_NODELAY;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.web.reactive.function.client.WebClient.builder;
import static reactor.util.Loggers.getLogger;

/**
 * reactive rest generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class BlueRestGenerator {

    private static final Logger LOGGER = getLogger(BlueRestGenerator.class);

    private static final String PROVIDER_NAME = "httpClient";

    private static final String RESOURCE_NAME_PREFIX = "react-rest-";

    private static final boolean DAEMON = true;

    public static WebClient generateWebClient(RestConf restConf) {
        if (isNull(restConf))
            throw new RuntimeException("restConf can't be null");

        LOGGER.info("restConf = {}", restConf);

        ConnectionProvider connectionProvider = ConnectionProvider.create(PROVIDER_NAME,
                ofNullable(restConf.getMaxConnections()).filter(v -> v > 0).orElse(DEFAULT_MAX_CONNECTIONS));
        LoopResources loopResources = LoopResources.create(RESOURCE_NAME_PREFIX,
                ofNullable(restConf.getWorkerCount()).filter(v -> v > 0).orElse(DEFAULT_WORKER_COUNT), DAEMON);

        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setUseGlobalResources(ofNullable(restConf.getUseGlobalResources()).orElse(false));
        reactorResourceFactory.setConnectionProvider(connectionProvider);
        reactorResourceFactory.setLoopResources(loopResources);

        Function<HttpClient, HttpClient> mapper = httpClient -> {
            ofNullable(restConf.getConnectTimeoutMillis())
                    .ifPresent(connectTimeoutMillis -> httpClient.option(CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis));
            ofNullable(restConf.getUseTcpNoDelay())
                    .ifPresent(useTcpNoDelay -> httpClient.option(TCP_NODELAY, useTcpNoDelay));
            ofNullable(restConf.getProtocols()).filter(BlueChecker::isNotEmpty)
                    .ifPresent(protocols -> httpClient.protocol(protocols.toArray(HttpProtocol[]::new)));
            ofNullable(restConf.getResponseTimeoutMillis())
                    .ifPresent(responseTimeoutMillis -> httpClient.responseTimeout(Duration.of(responseTimeoutMillis, MILLIS)));

            httpClient.doOnConnected(
                    connection -> connection
                            .addHandlerFirst(new ReadTimeoutHandler(
                                    ofNullable(restConf.getReadTimeoutMillis()).filter(v -> v > 0).orElse(DEFAULT_READ_TIMEOUT_MILLIS), MILLISECONDS))
                            .addHandlerFirst(new WriteTimeoutHandler(
                                    ofNullable(restConf.getWriteTimeoutMillis()).filter(v -> v > 0).orElse(DEFAULT_WRITE_TIMEOUT_MILLIS), MILLISECONDS)));

            return httpClient;
        };

        ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(reactorResourceFactory, mapper);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer.defaultCodecs()
                                .maxInMemorySize(ofNullable(restConf.getMaxByteInMemorySize()).filter(v -> v > 0).orElse(DEFAULT_MAX_BYTE_IN_MEMORY_SIZE))
                ).build();

        WebClient.Builder builder = builder()
                .clientConnector(reactorClientHttpConnector)
                .exchangeStrategies(exchangeStrategies);

        ofNullable(restConf.getDefaultHeaders())
                .filter(BlueChecker::isNotEmpty)
                .ifPresent(defaultHeaders ->
                        builder.defaultHeaders(httpHeaders -> defaultHeaders.forEach(httpHeaders::add)));

        return builder.build();
    }

}
