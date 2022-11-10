package com.blue.performance.service.shine;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.PointInTimeReference;
import co.elastic.clients.transport.TransportOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.blue.basic.common.base.BlueChecker;
import com.blue.es.api.conf.BaseEsConfParams;
import com.blue.es.api.conf.EsConf;
import com.blue.es.api.conf.EsNode;
import com.blue.es.api.conf.Server;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.blue.es.api.generator.BlueEsGenerator.generateElasticsearchAsyncClient;
import static com.blue.es.api.generator.BlueEsGenerator.generateRestClientTransport;
import static com.blue.es.common.EsSearchAfterProcessor.packageSearchAfter;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class Search {

    private static final ElasticsearchAsyncClient ELASTICSEARCH_ASYNC_CLIENT;

    private static final String INDEX_NAME = "shine";

    static {
        EsConf esConf = initConf();
        RestClientTransport restClientTransport = generateRestClientTransport(esConf);
        ELASTICSEARCH_ASYNC_CLIENT = generateElasticsearchAsyncClient(restClientTransport, esConf);
    }

    public static void main(String[] args) {
        test7();
    }

    private static void test7() {
        Query query = Query.of(b -> b.bool(new BoolQuery.Builder().must(FuzzyQuery.of(q -> q.field("content").value("容").fuzziness("0"))._toQuery()).build()));
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field("id").order(SortOrder.Desc))));

        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(INDEX_NAME)
                .query(query)
                .sort(sortOptions)
                .aggregations("group", ab -> ab.terms(tb -> tb.field("stateId")))
        );

        CompletableFuture<SearchResponse<Shine>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, Shine.class);
        SearchResponse<Shine> searchResponse = responseFuture.join();

        System.err.println(searchResponse.took());
        System.err.println();
        System.err.println(searchResponse.hits().total());
        System.err.println();

        searchResponse.hits().hits().forEach(h -> {
            Shine source = h.source();
            System.err.println(source);
            System.err.println();
        });

        Map<String, Aggregate> aggregations = searchResponse.aggregations();
        Aggregate aggregate = aggregations.get("group");
        System.err.println(aggregate);
    }

    private static void test6() {
        Query query = Query.of(b -> b.bool(new BoolQuery.Builder().must(FuzzyQuery.of(q -> q.field("content").value("容").fuzziness("0"))._toQuery()).build()));
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field("id").order(SortOrder.Desc))));

        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(INDEX_NAME)
                .query(query)
                .sort(sortOptions)
                .aggregations("max", ab -> ab.max(mb -> mb.field("stateId")))
        );

        CompletableFuture<SearchResponse<Shine>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, Shine.class);
        SearchResponse<Shine> searchResponse = responseFuture.join();

        System.err.println(searchResponse.took());
        System.err.println();
        System.err.println(searchResponse.hits().total());
        System.err.println();

        searchResponse.hits().hits().forEach(h -> {
            Shine source = h.source();
            System.err.println(source);
            System.err.println();
        });

        Map<String, Aggregate> aggregations = searchResponse.aggregations();
        Aggregate aggregate = aggregations.get("max");
        System.err.println(aggregate);
    }

    private static void test5() {
        Query query = Query.of(b -> b.bool(new BoolQuery.Builder().must(FuzzyQuery.of(q -> q.field("content").value("容").fuzziness("0"))._toQuery()).build()));
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field("id").order(SortOrder.Desc))));

        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(INDEX_NAME)
                .query(query)
                .sort(sortOptions)
                .highlight(Highlight.of(b -> b.fields("content", f -> f.preTags("<font color='red'>")
                        .postTags("</font>")))));

        CompletableFuture<SearchResponse<Shine>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, Shine.class);
        SearchResponse<Shine> searchResponse = responseFuture.join();

        System.err.println(searchResponse.took());
        System.err.println();
        System.err.println(searchResponse.hits().total());
        System.err.println();

        List<Hit<Shine>> hits = searchResponse.hits().hits();

        searchResponse.hits().hits().forEach(h -> {
            Map<String, List<String>> highlight = h.highlight();
            Shine source = h.source();
            source.setContent(highlight.get("content").get(0));

            System.err.println(source);
            System.err.println();
        });


    }

    private static void test4() {
        Time time = Time.of(builder -> builder.time("1m"));
        OpenPointInTimeRequest openPointInTimeRequest = OpenPointInTimeRequest.of(builder -> builder.index(INDEX_NAME).keepAlive(time));
        CompletableFuture<OpenPointInTimeResponse> pitResponseFuture = ELASTICSEARCH_ASYNC_CLIENT.openPointInTime(openPointInTimeRequest);

        OpenPointInTimeResponse pitResponse = pitResponseFuture.join();

        System.err.println(pitResponse);
        String pid = pitResponse.id();
        System.err.println(pid);

        PointInTimeReference pointInTimeReference = PointInTimeReference.of(builder -> builder.keepAlive(time).id(pid));
        Query query = Query.of(b -> b.bool(new BoolQuery.Builder().must(MatchQuery.of(q -> q.field("content").query("内容"))._toQuery()).build()));
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field("id").order(SortOrder.Desc))));

        List<String> searchAfter = null;

        SearchRequest searchRequest;
        for (int i = 0; i < 10; i++) {
            List<String> after = searchAfter;
            searchRequest = SearchRequest.of(builder -> {
                builder
                        .index(INDEX_NAME)
                        .query(query)
                        .sort(sortOptions)
                        .pit(pointInTimeReference)
                        .from(0)
                        .size(3);

                packageSearchAfter(builder, after.stream().map(FieldValue::of).collect(toList()));

                return builder;
            });

            CompletableFuture<SearchResponse<Shine>> dataResponseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, Shine.class);
            SearchResponse<Shine> dataResponse = dataResponseFuture.join();
            System.err.println("pit id = " + dataResponse.pitId());

            HitsMetadata<Shine> hits = dataResponse.hits();
            List<Hit<Shine>> hitList = hits.hits();

            for (Hit<Shine> hit : hitList) {

                System.err.println("shine id = " + hit.source().getId());
                System.err.println(hit.source().getTitle());
                System.err.println(hit.sort());
                searchAfter = ofNullable(hit.sort()).filter(BlueChecker::isNotEmpty).map(
                        l -> l.stream().map(FieldValue::stringValue).collect(toList())
                ).orElseGet(Collections::emptyList);
                System.err.println(searchAfter);

                System.err.println();
            }
        }
    }

    private static void test3() {
        Query query = Query.of(b -> b.bool(new BoolQuery.Builder().must(MatchQuery.of(q -> q.field("content").query("内容"))._toQuery()).build()));
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field("id").order(SortOrder.Desc))));

        List<String> searchAfter = null;

        SearchRequest searchRequest;
        for (int i = 0; i < 10; i++) {
            List<String> after = searchAfter;
            searchRequest = SearchRequest.of(builder -> {
                builder
                        .index(INDEX_NAME)
                        .query(query)
                        .sort(sortOptions)
                        .from(0)
                        .size(3);

                packageSearchAfter(builder, after.stream().map(FieldValue::of).collect(toList()));

                return builder;
            });

            CompletableFuture<SearchResponse<Shine>> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.search(searchRequest, Shine.class);
            SearchResponse<Shine> response = responseFuture.join();

            HitsMetadata<Shine> hits = response.hits();
            List<Hit<Shine>> hitList = hits.hits();

            for (Hit<Shine> hit : hitList) {

                System.err.println(hit.source().getId());
                System.err.println(hit.source().getTitle());
                System.err.println(hit.sort());
                ofNullable(hit.sort()).filter(BlueChecker::isNotEmpty).map(
                        l -> l.stream().map(FieldValue::stringValue).collect(toList())
                ).orElseGet(Collections::emptyList);
                System.err.println(searchAfter);

                System.err.println();
            }
        }
    }

    private static void test2() {
        ClosePointInTimeRequest closePointInTimeRequest = ClosePointInTimeRequest.of(builder ->
                builder.id("v5HqAwEFc2hpbmUWVmdWV01QTTdRTkdEQ3NPRzFIVDJndwAWTndzUlBsdGNUbEs2cWF5WklOWXZ4dwAAAAAAAAAAORZYOWQ4VkpQdVJVV19fYUVUZkd5bXBBAAEWVmdWV01QTTdRTkdEQ3NPRzFIVDJndwAA"));
        CompletableFuture<ClosePointInTimeResponse> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.closePointInTime(closePointInTimeRequest);

        ClosePointInTimeResponse response = responseFuture.join();

        System.err.println(response);
        boolean succeeded = response.succeeded();
        System.err.println(succeeded);
        int numFreed = response.numFreed();
        System.err.println(numFreed);
    }

    private static void test1() {
        Time time = Time.of(builder -> builder.time("1m"));
        OpenPointInTimeRequest openPointInTimeRequest = OpenPointInTimeRequest.of(builder -> builder.index(INDEX_NAME).keepAlive(time));
        CompletableFuture<OpenPointInTimeResponse> responseFuture = ELASTICSEARCH_ASYNC_CLIENT.openPointInTime(openPointInTimeRequest);

        OpenPointInTimeResponse response = responseFuture.join();

        System.err.println(response);
        String id = response.id();
        System.err.println(id);
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
