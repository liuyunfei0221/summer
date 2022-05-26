package com.blue.shine.service.impl;

import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.mongo.component.CollectionGetter;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.repository.template.ShineRepository;
import com.blue.shine.service.inter.ShineService;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * shine service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class ShineServiceImpl implements ShineService {

    private static final Logger LOGGER = getLogger(ShineServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ShineRepository shineRepository;

    private final CollectionGetter collectionGetter;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ShineRepository shineRepository, CollectionGetter collectionGetter) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.shineRepository = shineRepository;
        this.collectionGetter = collectionGetter;
    }


    @PostConstruct
    private void init() {
        long now = Instant.now().getEpochSecond();

        Shine s1 = new Shine(blueIdentityProcessor.generate(Shine.class), "title1", "content1", 1, now, now, 1L, 1L);
        Shine s2 = new Shine(blueIdentityProcessor.generate(Shine.class), "title2", "content2", 1, now, now, 2L, 2L);
        Shine s3 = new Shine(blueIdentityProcessor.generate(Shine.class), "title3", "content3", 2, now, now, 3L, 3L);
        Shine s4 = new Shine(blueIdentityProcessor.generate(Shine.class), "title4", "content4", 2, now, now, 4L, 4L);
        Shine s5 = new Shine(blueIdentityProcessor.generate(Shine.class), "title5", "content5", 3, now, now, 5L, 5L);

        shineRepository.saveAll(Stream.of(s1, s2, s3, s4, s5).collect(toList())).subscribe(System.err::println);
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

        MongoCollection<Document> mongoCollection = collectionGetter.getCollection(Shine.class);

        Bson filter = Filters.eq("order", 1);

        Bson query = combine(set("title", "R"), set("content", "R"));

        UpdateOptions options = new UpdateOptions().upsert(true);

        Publisher<UpdateResult> updateResultPublisher = mongoCollection.updateMany(filter, query, options);

        Mono<UpdateResult> updateResultMono = Mono.from(updateResultPublisher);

        return updateResultMono.flatMap(updateResult -> {

            long matchedCount = updateResult.getMatchedCount();
            long modifiedCount = updateResult.getModifiedCount();
            boolean wasAcknowledged = updateResult.wasAcknowledged();


            System.err.println(matchedCount);
            System.err.println(modifiedCount);
            System.err.println(wasAcknowledged);

            return shineRepository.findAll()
                    .take(3)
                    .collectList()
                    .flatMap(shines ->
                            just(shines.stream()
                                    .map(shine -> new ShineInfo(shine.getId(), shine.getTitle(), shine.getContent(), shine.getOrder(), shine.getCreateTime()
                                    )).collect(toList())));
        });
    }

}
