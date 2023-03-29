package com.blue.es.api.generator;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.blue.basic.common.base.BlueChecker;
import com.blue.es.api.conf.EsConf;
import com.blue.es.api.conf.EsNode;
import com.blue.es.api.conf.Server;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * es components generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SuspiciousToArrayCall", "AliControlFlowStatementWithoutBraces"})
public final class BlueEsGenerator {

    private static final Logger LOGGER = getLogger(BlueEsGenerator.class);

    private static final String DEFAULT_PATH_PREFIX = "/";

    private static final String CERT_TYPE = "X.509";
    private static final String KEY_STORE_TYPE = "pkcs12";
    private static final String CA_ALIAS = "ca";


    private static final Function<EsConf, RestClient> REST_CLIENT_GENERATOR = esConf -> {
        LOGGER.info("esConf = {}", esConf);
        assertConf(esConf);

        List<Node> nodeList = esConf.getEsNodes()
                .stream().map(esNode -> {

                    Server server = esNode.getServer();
                    if (isNull(server))
                        throw new RuntimeException("server can't be null");

                    String serverHost = server.getHost();
                    Integer serverPort = server.getPort();
                    String serverSchema = server.getSchema();

                    if (isBlank(serverHost) || isBlank(serverSchema) || isNull(serverPort) || serverPort < 1)
                        throw new RuntimeException("serverHost or serverSchema can't be blank, serverPort can't be null or less than 1");

                    HttpHost host = new HttpHost(serverHost, serverPort, serverSchema);

                    Set<HttpHost> boundHosts = ofNullable(esNode.getBoundServers())
                            .map(bs -> bs.stream().filter(Objects::nonNull)
                                    .collect(toList())
                            ).filter(bs -> bs.size() > 0)
                            .map(bs -> bs.stream().map(s ->
                                            new HttpHost(s.getHost(), s.getPort(), s.getSchema()))
                                    .collect(toSet())
                            ).orElseGet(HashSet::new);

                    Node.Roles roles = ofNullable(esNode.getRoles())
                            .filter(rs -> rs.size() > 0)
                            .map(HashSet::new)
                            .map(Node.Roles::new)
                            .orElseGet(() -> new Node.Roles(new HashSet<>()));

                    Map<String, List<String>> attributes = ofNullable(esNode.getAttributes()).orElseGet(HashMap::new);

                    return new Node(host, boundHosts, esNode.getName(), esNode.getVersion(), roles, attributes);
                }).collect(toList());

        RestClientBuilder builder = RestClient.builder(nodeList.toArray(Node[]::new));

        if (ofNullable(esConf.getAuth()).orElse(false))
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                ofNullable(esConf.getUserName())
                        .filter(BlueChecker::isNotBlank)
                        .ifPresent(userName -> {
                            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, esConf.getPassword()));
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        });

                ofNullable(esConf.getCertPath())
                        .filter(BlueChecker::isNotBlank)
                        .ifPresent(certPath -> {
                            Path path = Paths.get(certPath);
                            try (InputStream inputStream = Files.newInputStream(path)) {
                                CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
                                Certificate certificate = certificateFactory.generateCertificate(inputStream);

                                KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
                                keyStore.load(null, null);
                                keyStore.setCertificateEntry(CA_ALIAS, certificate);

                                httpClientBuilder.setSSLContext(SSLContexts.custom().loadTrustMaterial(keyStore, null).build());
                            } catch (Exception e) {
                                throw new RuntimeException("", e);
                            }
                        });

                return httpClientBuilder;
            });

        ofNullable(esConf.getDefaultHeaders())
                .ifPresent(hs -> builder.setDefaultHeaders(
                        hs.stream()
                                .map(h -> new BasicHeaderElement(h.getName(), h.getValue()))
                                .collect(toList()).toArray(Header[]::new)));

        ofNullable(esConf.getFailureListener())
                .ifPresent(builder::setFailureListener);
        ofNullable(esConf.getHttpClientConfigCallback())
                .ifPresent(builder::setHttpClientConfigCallback);
        ofNullable(esConf.getRequestConfigCallback())
                .ifPresent(builder::setRequestConfigCallback);
        builder.setPathPrefix(ofNullable(esConf.getPathPrefix()).orElse(DEFAULT_PATH_PREFIX));
        ofNullable(esConf.getStrictDeprecationMode())
                .ifPresent(builder::setStrictDeprecationMode);
        ofNullable(esConf.getMetaHeaderEnabled())
                .ifPresent(builder::setMetaHeaderEnabled);
        ofNullable(esConf.getCompressionEnabled())
                .ifPresent(builder::setCompressionEnabled);
        ofNullable(esConf.getNodeSelector())
                .ifPresent(builder::setNodeSelector);

        return builder.build();
    };

    /**
     * generate transport
     *
     * @param esConf
     * @return
     */
    public static RestClientTransport generateRestClientTransport(EsConf esConf) {
        LOGGER.info("esConf = {}", esConf);
        if (isNull(esConf))
            throw new RuntimeException("esConf can't be null");

        return new RestClientTransport(REST_CLIENT_GENERATOR.apply(esConf),
                new JacksonJsonpMapper(), esConf.getTransportOptions());
    }

    /**
     * generate client
     *
     * @param esConf
     * @return
     */
    public static ElasticsearchClient generateElasticsearchClient(RestClientTransport restClientTransport, EsConf esConf) {
        LOGGER.info("restClientTransport = {}, esConf = {}", restClientTransport, esConf);
        if (isNull(restClientTransport) || isNull(esConf))
            throw new RuntimeException("restClientTransport or esConf can't be null");

        return new ElasticsearchClient(restClientTransport, esConf.getTransportOptions());
    }

    /**
     * generate async client
     *
     * @param esConf
     * @return
     */
    public static ElasticsearchAsyncClient generateElasticsearchAsyncClient(RestClientTransport restClientTransport, EsConf esConf) {
        LOGGER.info("restClientTransport = {}, esConf = {}", restClientTransport, esConf);
        if (isNull(restClientTransport) || isNull(esConf))
            throw new RuntimeException("restClientTransport or esConf can't be null");

        return new ElasticsearchAsyncClient(restClientTransport, esConf.getTransportOptions());
    }

    /**
     * assert param
     *
     * @param esNode
     */
    private static void nodeAsserter(EsNode esNode) {
        if (isNull(esNode.getServer()))
            throw new RuntimeException("server can't be null");
    }

    /**
     * assert param
     *
     * @param conf
     */
    private static void assertConf(EsConf conf) {
        if (isNull(conf))
            throw new RuntimeException("conf can't be null");

        List<EsNode> esNodes = conf.getEsNodes();
        if (isEmpty(esNodes))
            throw new RuntimeException("esNodes can't be empty");

        esNodes.forEach(BlueEsGenerator::nodeAsserter);
    }

}
