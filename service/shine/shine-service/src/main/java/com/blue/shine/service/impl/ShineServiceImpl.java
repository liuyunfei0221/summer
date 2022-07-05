package com.blue.shine.service.impl;

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
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
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

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ShineRepository shineRepository,
                            ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.shineRepository = shineRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
    }


    @PostConstruct
    private void init() {
        Long stamp = TIME_STAMP_GETTER.get();

        Shine s1 = new Shine(blueIdentityProcessor.generate(Shine.class), "title1", "content1", 1, stamp, stamp, 1L, 1L);
        Shine s2 = new Shine(blueIdentityProcessor.generate(Shine.class), "title2", "content2", 1, stamp, stamp, 2L, 2L);
        Shine s3 = new Shine(blueIdentityProcessor.generate(Shine.class), "title3", "content3", 2, stamp, stamp, 3L, 3L);
        Shine s4 = new Shine(blueIdentityProcessor.generate(Shine.class), "title4", "content4", 2, stamp, stamp, 4L, 4L);
        Shine s5 = new Shine(blueIdentityProcessor.generate(Shine.class), "title5", "content5", 3, stamp, stamp, 5L, 5L);

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
