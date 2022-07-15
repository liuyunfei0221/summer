package com.blue.basic.component.rest.api.generator;

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
import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.channel.ChannelOption.TCP_NODELAY;
import static java.time.temporal.ChronoUnit.MILLIS;
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

        LOGGER.info("WebClient generateWebClient(RestConf restConf), restConf = {}", restConf);

        ConnectionProvider connectionProvider = ConnectionProvider.create(PROVIDER_NAME, restConf.getMaxConnections());
        LoopResources loopResources = LoopResources.create(RESOURCE_NAME_PREFIX, restConf.getWorkerCount(), DAEMON);

        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setUseGlobalResources(restConf.getUseGlobalResources());
        reactorResourceFactory.setConnectionProvider(connectionProvider);
        reactorResourceFactory.setLoopResources(loopResources);

        Function<HttpClient, HttpClient> mapper = httpClient ->
                httpClient
                        .option(CONNECT_TIMEOUT_MILLIS, restConf.getConnectTimeoutMillis())
                        .option(TCP_NODELAY, restConf.getUseTcpNoDelay())
                        .protocol(restConf.getProtocols().toArray(HttpProtocol[]::new))
                        .responseTimeout(Duration.of(restConf.getResponseTimeoutMillis(), MILLIS))
                        .doOnConnected(
                                connection -> connection
                                        .addHandlerFirst(new ReadTimeoutHandler(restConf.getReadTimeoutMillis(), MILLISECONDS))
                                        .addHandlerFirst(new WriteTimeoutHandler(restConf.getWriteTimeoutMillis(), MILLISECONDS)));

        ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(reactorResourceFactory, mapper);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(restConf.getMaxByteInMemorySize())
                ).build();

        return builder()
                .clientConnector(reactorClientHttpConnector)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

}
