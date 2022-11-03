package com.blue.base.service.impl;

import com.blue.base.api.model.DictInfo;
import com.blue.base.api.model.DictTypeInfo;
import com.blue.base.config.deploy.DictCaffeineDeploy;
import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import com.blue.base.repository.template.DictRepository;
import com.blue.base.repository.template.DictTypeRepository;
import com.blue.base.service.inter.DictService;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;

import static com.blue.base.constant.BaseColumnName.NAME;
import static com.blue.base.converter.BaseModelConverters.DICT_2_DICT_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.DICT_TYPES_2_DICT_TYPE_INFOS_CONVERTER;
import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * dict service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class DictServiceImpl implements DictService {

    private static final Logger LOGGER = getLogger(DictServiceImpl.class);

    private final DictTypeRepository dictTypeRepository;

    private final DictRepository dictRepository;

    public DictServiceImpl(ExecutorService executorService, DictTypeRepository dictTypeRepository, DictRepository dictRepository, DictCaffeineDeploy dictCaffeineDeploy) {
        this.dictTypeRepository = dictTypeRepository;
        this.dictRepository = dictRepository;

        allTypesCache = generateCacheAsyncCache(new CaffeineConfParams(
                dictCaffeineDeploy.getDictTypeMaximumSize(), Duration.of(dictCaffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));

        typeCodeDictCache = generateCacheAsyncCache(new CaffeineConfParams(
                dictCaffeineDeploy.getDictMaximumSize(), Duration.of(dictCaffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static final String ALL_TYPES_CACHE_KEY = "THE_TYPES";

    private final AsyncCache<String, List<DictTypeInfo>> allTypesCache;

    private final AsyncCache<String, List<DictInfo>> typeCodeDictCache;

    private final BiFunction<String, Executor, CompletableFuture<List<DictTypeInfo>>> DB_TYPE_GETTER = (theKey, executor) ->
            this.selectDictType().map(DICT_TYPES_2_DICT_TYPE_INFOS_CONVERTER).toFuture();

    private final BiFunction<String, Executor, CompletableFuture<List<DictInfo>>> DB_DICT_GETTER = (code, executor) -> {
        if (isBlank(code))
            throw new BlueException(INVALID_PARAM);

        return this.selectDictByTypeCode(code).map(DICT_2_DICT_INFOS_CONVERTER).toFuture();
    };

    /**
     * select all dict type
     *
     * @return
     */
    @Override
    public Mono<List<DictType>> selectDictType() {
        return dictTypeRepository.findAll(Sort.by(Sort.Order.asc(NAME.name))).collectList();
    }

    /**
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDictByTypeCode(String code) {
        LOGGER.info("code = {}", code);
        if (isBlank(code))
            throw new BlueException(INVALID_PARAM);

        DictType dictTypeProbe = new DictType();
        dictTypeProbe.setCode(code);

        return dictTypeRepository.findOne(Example.of(dictTypeProbe)).
                map(DictType::getId)
                .map(tid -> {
                    Dict dictProbe = new Dict();
                    dictProbe.setDictTypeId(tid);
                    return dictRepository.findAll(Example.of(dictProbe), Sort.by(Sort.Order.asc(NAME.name))).collectList().toFuture().join();
                })
                .switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select all dict types
     *
     * @return
     */
    @Override
    public Mono<List<DictTypeInfo>> selectDictTypeInfo() {
        return fromFuture(allTypesCache.get(ALL_TYPES_CACHE_KEY, DB_TYPE_GETTER)).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select all dict types
     *
     * @return
     */
    @Override
    public Mono<List<DictInfo>> selectDictInfoByTypeCode(String code) {
        LOGGER.info("code = {}", code);

        return isNotBlank(code)
                ?
                fromFuture(typeCodeDictCache.get(code, DB_DICT_GETTER)).switchIfEmpty(defer(() -> just(emptyList())))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid cache
     *
     * @return
     */
    @Override
    public void invalidCache() {
        LOGGER.info("void invalidCache()");

        allTypesCache.synchronous().invalidateAll();
        typeCodeDictCache.synchronous().invalidateAll();
    }

}
