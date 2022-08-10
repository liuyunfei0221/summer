package com.blue.shine.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.repository.template.ShineRepository;
import com.blue.shine.service.inter.ShineService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * shine service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class ShineServiceImpl implements ShineService {

    private static final Logger LOGGER = getLogger(ShineServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ShineRepository shineRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private final ElasticsearchClient elasticsearchClient;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ShineRepository shineRepository, ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, ElasticsearchClient elasticsearchClient) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.shineRepository = shineRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    private void init() {

        Long stamp = TIME_STAMP_GETTER.get();

        List<Shine> list = new LinkedList<>();

        for (int i = 0; i <= 1000; i++) {
            long id = blueIdentityProcessor.generate(Shine.class);
            Shine shine = new Shine(id, "title-" + id, "content-" + id, 1, stamp, stamp, 1L, 1L);

            try {
                IndexResponse indexResponse = elasticsearchClient.index(idx ->
                        idx.index("shine")
                                .id(String.valueOf(id))
                                .document(shine));
                System.err.println(indexResponse);
            } catch (Exception e) {
                LOGGER.error("index() failed, e = {0}", e);
            }

            list.add(shine);
        }

        shineRepository.saveAll(list).subscribe(System.err::println);
    }

    /**
     * select shine info
     *
     * @param ip
     * @return
     */
    @Override
    public Mono<List<ShineInfo>> selectShineInfo(String ip) {
        LOGGER.warn("ip = {}", ip);

        String contentLike = "ten";
        Pattern pattern = Pattern.compile("^.*" + contentLike + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        query.addCriteria(Criteria.where("content").regex(pattern));

        query.skip(5).limit(3);

        Flux<Shine> shineFlux = reactiveMongoTemplate.find(query, Shine.class).publishOn(scheduler);
        List<Shine> shineList = shineFlux.collectList().toFuture().join();
        System.err.println(shineList);

        return shineRepository.findAll()
                .take(3)
                .collectList()
                .flatMap(shines ->
                        just(shines.stream()
                                .map(shine -> new ShineInfo(shine.getId(), shine.getTitle(), shine.getContent(), shine.getOrder(), shine.getCreateTime()
                                )).collect(toList())));
    }

}
