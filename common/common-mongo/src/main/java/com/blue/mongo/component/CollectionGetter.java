package com.blue.mongo.component;

import com.blue.base.model.exps.BlueException;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.substring;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * Collection getter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class CollectionGetter {

    private static final Logger LOGGER = getLogger(CollectionGetter.class);

    private ReactiveMongoTemplate reactiveMongoTemplate;

    public CollectionGetter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    /**
     * collections holder
     */
    private final Map<String, MongoCollection<Document>> COLLECTIONS = new ConcurrentHashMap<>();

    /**
     * collection getter
     */
    private final Function<String, MongoCollection<Document>> COLLECTION_GETTER = key -> {
        MongoCollection<Document> collection = COLLECTIONS.get(key.intern());
        if (isNotNull(collection))
            return collection;

        synchronized (key.intern()) {
            if (isNull((collection = COLLECTIONS.get(key)))) {
                collection = reactiveMongoTemplate.getCollection(key).toFuture().join();
                COLLECTIONS.put(key, collection);
                LOGGER.info("collection init, key = {}", key);
            }
        }

        return collection;
    };

    /**
     * collection name getter
     */
    private final Function<Class<?>, String> COLLECTION_NAME_GETTER = entityClz -> {
        if (isNull(entityClz))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "entityClz can't be null");

        String simpleClzName = entityClz.getSimpleName();
        if (isBlank(simpleClzName))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "entityClz can't be null");

        return lowerCase(substring(simpleClzName, 0, 1)) + substring(simpleClzName, 1);
    };

    /**
     * get collection
     *
     * @param entityClz
     * @return
     */
    public MongoCollection<Document> getCollection(Class<?> entityClz) {
        return COLLECTION_GETTER.apply(COLLECTION_NAME_GETTER.apply(entityClz).intern());
    }

    /**
     * get collection mono
     *
     * @param entityClz
     * @return
     */
    public Mono<MongoCollection<Document>> getCollectionReact(Class<?> entityClz) {
        return just(getCollection(entityClz));
    }

}
