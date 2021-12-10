package com.blue.es.api.generator;

import com.blue.base.model.exps.BlueException;
import com.blue.es.api.conf.EsConf;
import com.blue.es.api.conf.EsNode;
import com.blue.es.api.conf.Server;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeaderElement;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import reactor.util.Logger;

import java.util.*;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * es components generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "SuspiciousToArrayCall", "AliControlFlowStatementWithoutBraces"})
public final class BlueEsGenerator {

    private static final Logger LOGGER = getLogger(BlueEsGenerator.class);

    private static final String DEFAULT_PATH_PREFIX = "/";

    /**
     * generate client
     *
     * @param esConf
     * @return
     */
    public static RestHighLevelClient generateRestHighLevelClient(EsConf esConf) {
        LOGGER.info("RestHighLevelClient generateRestHighLevelClient(EsConf esConf), esConf = {}", esConf);

        List<Node> nodeList = esConf.getEsNodes()
                .stream().map(esNode -> {

                    Server server = esNode.getServer();
                    if (server == null)
                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "server can't be null", null);

                    String serverHost = server.getHost();
                    Integer serverPort = server.getPort();
                    String serverSchema = server.getSchema();

                    if (isBlank(serverHost) || isBlank(serverSchema) || serverPort == null || serverPort < 1)
                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverHost or serverSchema can't be blank, serverPort can't be null or less than 1", null);

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
                            .orElse(new Node.Roles(new HashSet<>()));

                    Map<String, List<String>> attributes = ofNullable(esNode.getAttributes()).orElseGet(HashMap::new);

                    return new Node(host, boundHosts, esNode.getName(), esNode.getVersion(), roles, attributes);
                }).collect(toList());

        RestClientBuilder builder = RestClient.builder(nodeList.toArray(Node[]::new));

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

        return new RestHighLevelClient(builder);
    }

    /**
     * generate es template
     *
     * @param restHighLevelClient
     * @return
     */
    public static ElasticsearchRestTemplate generateElasticsearchTemplate(RestHighLevelClient restHighLevelClient) {
        if (restHighLevelClient == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "restHighLevelClient can't be null", null);
        return new ElasticsearchRestTemplate(restHighLevelClient);
    }

    /**
     * assert param
     *
     * @param conf
     */
    private static void confAsserter(EsConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null", null);

        List<EsNode> esNodes = conf.getEsNodes();
        if (isEmpty(esNodes))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "esNodes can't be empty", null);
    }

}
