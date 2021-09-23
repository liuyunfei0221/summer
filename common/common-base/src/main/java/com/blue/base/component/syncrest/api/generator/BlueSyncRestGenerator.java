package com.blue.base.component.syncrest.api.generator;

import com.blue.base.component.syncrest.api.conf.SyncRestConf;
import io.netty.channel.ConnectTimeoutException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import reactor.util.Logger;

import java.net.SocketTimeoutException;

import static java.nio.charset.CodingErrorAction.IGNORE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
public class BlueSyncRestGenerator {

    private static final Logger LOGGER = getLogger(BlueSyncRestGenerator.class);

    private static final BasicCookieStore COOKIE_STORE = new BasicCookieStore();

    private static final String DEFAULT_USER_AGENT = "summer";

    private static final TrustStrategy TRUST_STRATEGY = new TrustAllStrategy();

    public static RestTemplate createRestTemplate(SyncRestConf syncRestConf) {
        try {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", new PlainConnectionSocketFactory())
                            .register("https",
                                    new SSLConnectionSocketFactory(
                                            SSLContexts.custom().loadTrustMaterial(TRUST_STRATEGY).build(),
                                            NoopHostnameVerifier.INSTANCE))
                            .build());

            ofNullable(syncRestConf.getMaxPerRoute()).ifPresent(connectionManager::setDefaultMaxPerRoute);
            ofNullable(syncRestConf.getMaxTotal()).ifPresent(connectionManager::setMaxTotal);

            SocketConfig.Builder socketConfBuilder = SocketConfig.custom();
            ofNullable(syncRestConf.getSoTimeout()).ifPresent(socketConfBuilder::setSoTimeout);
            ofNullable(syncRestConf.getTcpNoDelay()).ifPresent(socketConfBuilder::setTcpNoDelay);
            ofNullable(syncRestConf.getSoKeepAlive()).ifPresent(socketConfBuilder::setSoKeepAlive);
            ofNullable(syncRestConf.getSoReuseAddress()).ifPresent(socketConfBuilder::setSoReuseAddress);
            connectionManager.setDefaultSocketConfig(socketConfBuilder.build());

            RequestConfig.Builder requestConfBuilder = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD);
            ofNullable(syncRestConf.getConnectTimeout()).ifPresent(requestConfBuilder::setConnectTimeout);
            ofNullable(syncRestConf.getSoTimeout()).ifPresent(requestConfBuilder::setSocketTimeout);
            ofNullable(syncRestConf.getRequestTimeout()).ifPresent(requestConfBuilder::setConnectionRequestTimeout);
            ofNullable(syncRestConf.getExpectContinueEnabled()).ifPresent(requestConfBuilder::setExpectContinueEnabled);
            ofNullable(syncRestConf.getRedirectsEnabled()).ifPresent(requestConfBuilder::setRedirectsEnabled);
            ofNullable(syncRestConf.getCircularRedirectsAllowed()).ifPresent(requestConfBuilder::setCircularRedirectsAllowed);
            ofNullable(syncRestConf.getMaxRedirects()).ifPresent(requestConfBuilder::setMaxRedirects);
            RequestConfig requestConfig = requestConfBuilder.build();

            ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(IGNORE).setUnmappableInputAction(IGNORE).build();

            HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
                if (executionCount > syncRestConf.getRetryTimes()) {
                    LOGGER.info("重试次数过多", exception);
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    LOGGER.info("连接超时", exception);
                    return true;
                }
                if (exception instanceof SocketTimeoutException) {
                    LOGGER.info("连接超时", exception);
                    return false;
                }
                if (!(HttpClientContext.adapt(context).getRequest() instanceof HttpEntityEnclosingRequest)) {
                    LOGGER.info("幂等异常，重试中......", exception);
                    return true;
                }
                return syncRestConf.getRadicalizationTry();
            };

            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultConnectionConfig(connectionConfig)
                    .setRetryHandler(httpRequestRetryHandler)
                    .setDefaultCookieStore(COOKIE_STORE)
                    .setUserAgent(ofNullable(syncRestConf.getUserAgent()).orElse(DEFAULT_USER_AGENT))
                    .evictExpiredConnections()
                    .build();

            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            LOGGER.error("restTemplate 初始化失败,e = {0}", e);
            throw new RuntimeException("restTemplate 初始化失败,e = " + e);
        }
    }

}
