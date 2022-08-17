package com.blue.shine.demo;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.PointInTimeReference;
import co.elastic.clients.transport.TransportOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.blue.es.api.conf.BaseEsConfParams;
import com.blue.es.api.conf.EsConf;
import com.blue.es.api.conf.EsNode;
import com.blue.es.api.conf.Server;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.concurrent.CompletableFuture;

import static com.blue.es.api.generator.BlueEsGenerator.generateElasticsearchAsyncClient;
import static com.blue.es.api.generator.BlueEsGenerator.generateRestClientTransport;
import static com.blue.shine.constant.ShineTableName.SHINE;
import static java.util.Collections.singletonList;

@SuppressWarnings("DuplicatedCode")
public class Search {

    private static final ElasticsearchAsyncClient ELASTICSEARCH_ASYNC_CLIENT;

    private static final String INDEX_NAME = SHINE.name;

    static {
        EsConf esConf = initConf();
        RestClientTransport restClientTransport = generateRestClientTransport(esConf);
        ELASTICSEARCH_ASYNC_CLIENT = generateElasticsearchAsyncClient(restClientTransport, esConf);
    }

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
//        SearchRequest searchRequest = SearchRequest.of(fn -> fn.index(INDEX_NAME).q("_pit?keep_alive=1m"));
        SearchRequest searchRequest = SearchRequest.of(fn -> fn.index(INDEX_NAME).q("_pit?keep_alive=1m"));

        CompletableFuture<SearchResponse<String>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest,String.class);
        SearchResponse<String> response = responseFuture.join();

        System.err.println(response);
        HitsMetadata<String> hits = response.hits();
        System.err.println(hits);
    }

    private static void test2() {
        Time time = Time.of(fn -> fn.time("1m"));
        PointInTimeReference pointInTimeReference = PointInTimeReference.of(fn -> fn.keepAlive(time));
        SearchRequest searchRequest = SearchRequest.of(fn -> fn.pit(pointInTimeReference));
        CompletableFuture<SearchResponse<String>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, String.class);
        SearchResponse<String> response = responseFuture.join();

        System.err.println(response);
        HitsMetadata<String> hits = response.hits();
        System.err.println(hits);
    }


    private static EsConf initConf() {
        BaseEsConfParams esConfParams = new BaseEsConfParams() {
            @Override
            public RestClient.FailureListener getFailureListener() {
                return null;
            }

            @Override
            public RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback() {
                return null;
            }

            @Override
            public RestClientBuilder.RequestConfigCallback getRequestConfigCallback() {
                return null;
            }

            @Override
            public NodeSelector getNodeSelector() {
                return null;
            }

            @Override
            public TransportOptions getTransportOptions() {
                return null;
            }
        };

        Server server = new Server();
        server.setHost("localhost");
        server.setPort(9200);
        server.setSchema("http");

        EsNode esNode = new EsNode();
        esNode.setServer(server);
        esNode.setName("blue-es");
        esNode.setVersion("8.3.3");

        esConfParams.setEsNodes(singletonList(esNode));
        esConfParams.setPathPrefix("/");
        esConfParams.setStrictDeprecationMode(false);
        esConfParams.setMetaHeaderEnabled(false);
        esConfParams.setCompressionEnabled(false);

        return esConfParams;
    }

}
