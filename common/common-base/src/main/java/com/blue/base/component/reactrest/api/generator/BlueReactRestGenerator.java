package com.blue.base.component.reactrest.api.generator;

import com.blue.base.component.reactrest.api.conf.ReactRestConf;
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

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.channel.ChannelOption.TCP_NODELAY;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.util.Loggers.getLogger;

/**
 * reactive rest generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
public final class BlueReactRestGenerator {

    private static final Logger LOGGER = getLogger(BlueReactRestGenerator.class);

    private static final String PROVIDER_NAME = "httpClient";

    private static final String RESOURCE_NAME_PREFIX = "react-rest-";

    private static final boolean DAEMON = true;

    public static WebClient createWebClient(ReactRestConf reactRestConf) {
        LOGGER.info("WebClient createWebClient(ReactRestConf reactRestConf), reactRestConf = {}", reactRestConf);

        ConnectionProvider connectionProvider = ConnectionProvider.create(PROVIDER_NAME, reactRestConf.getMaxConnections());
        LoopResources loopResources = LoopResources.create(RESOURCE_NAME_PREFIX, reactRestConf.getWorkerCount(), DAEMON);

        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setUseGlobalResources(reactRestConf.getUseGlobalResources());
        reactorResourceFactory.setConnectionProvider(connectionProvider);
        reactorResourceFactory.setLoopResources(loopResources);

        Function<HttpClient, HttpClient> mapper = httpClient ->
                httpClient
                        .option(CONNECT_TIMEOUT_MILLIS, reactRestConf.getConnectTimeoutMillis())
                        .option(TCP_NODELAY, reactRestConf.getUseTcpNodelay())
                        .protocol(reactRestConf.getProtocols().toArray(HttpProtocol[]::new))
                        .responseTimeout(Duration.of(reactRestConf.getReadTimeoutMillis(), MILLIS))
                        .doOnConnected(
                                connection -> connection
                                        .addHandler(new ReadTimeoutHandler(reactRestConf.getReadTimeoutMillis(), MILLISECONDS))
                                        .addHandler(new WriteTimeoutHandler(reactRestConf.getWriteTimeoutMillis(), MILLISECONDS)));

        ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(reactorResourceFactory, mapper);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(reactRestConf.getMaxByteInMemorySize())
                ).build();

        return WebClient.builder()
                .clientConnector(reactorClientHttpConnector)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

}
