package com.blue.base.service.impl;

import com.blue.base.api.model.DictInfo;
import com.blue.base.api.model.DictTypeInfo;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.config.deploy.DictCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import com.blue.base.repository.template.DictRepository;
import com.blue.base.repository.template.DictTypeRepository;
import com.blue.base.service.inter.DictService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.converter.BaseModelConverters.DICT_2_DICT_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.DICT_TYPES_2_DICT_TYPE_INFOS_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
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

    public DictServiceImpl(ExecutorService executorService, DictCaffeineDeploy dictCaffeineDeploy, DictTypeRepository dictTypeRepository, DictRepository dictRepository) {
        this.dictTypeRepository = dictTypeRepository;
        this.dictRepository = dictRepository;

        ALL_TYPES_CACHE = generateCache(new CaffeineConfParams(
                dictCaffeineDeploy.getDictTypeMaximumSize(), Duration.of(dictCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        TYPE_CODE_DICT_CACHE = generateCache(new CaffeineConfParams(
                dictCaffeineDeploy.getDictMaximumSize(), Duration.of(dictCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static final String ALL_TYPES_CACHE_KEY = "THE_TYPES";

    private static Cache<String, List<DictTypeInfo>> ALL_TYPES_CACHE;

    private static Cache<String, List<DictInfo>> TYPE_CODE_DICT_CACHE;

    private final Function<String, List<DictTypeInfo>> DB_TYPE_GETTER = theKey -> {
        List<DictType> types = this.selectDictType();

        LOGGER.info("DB_TYPE_GETTER, types = {}", types);
        return DICT_TYPES_2_DICT_TYPE_INFOS_CONVERTER.apply(
                types.stream().sorted(Comparator.comparing(DictType::getName)).collect(toList()));
    };

    private final Function<String, List<DictInfo>> DB_DICT_GETTER = code -> {
        List<Dict> dict = this.selectDictByTypeCode(code);

        LOGGER.info("DB_DICT_GETTER, code = {}, dict = {}", code, dict);
        return DICT_2_DICT_INFOS_CONVERTER.apply(
                dict.stream().sorted(Comparator.comparing(Dict::getName)).collect(toList()));
    };

    /**
     * select all dict type
     *
     * @return
     */
    @Override
    public List<DictType> selectDictType() {
        return dictTypeRepository.findAll(Sort.by(Sort.Order.asc("name"))).collectList().toFuture().join();
    }

    /**
     * select all dict
     *
     * @return
     */
    @Override
    public List<Dict> selectDict() {
        return dictRepository.findAll(Sort.by(Sort.Order.asc("name"))).collectList().toFuture().join();
    }

    /**
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    @Override
    public List<Dict> selectDictByTypeCode(String code) {
        LOGGER.info("Mono<List<Dict>> selectDictByTypeCode(String code), code = {}", code);

        return ofNullable(code)
                .filter(BlueChecker::isNotBlank)
                .map(c -> {
                    DictType probe = new DictType();
                    probe.setCode(c);
                    return dictTypeRepository.findOne(Example.of(probe)).toFuture().join();
                })
                .map(DictType::getId)
                .map(tid -> {
                    Dict probe = new Dict();
                    probe.setDictTypeId(tid);
                    return dictRepository.findAll(Example.of(probe), Sort.by(Sort.Order.asc("name"))).collectList().toFuture().join();
                })
                .orElseThrow(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * select all dict types
     *
     * @return
     */
    @Override
    public Mono<List<DictTypeInfo>> selectDictTypeInfo() {
        LOGGER.info("Mono<List<DictTypeInfo>> selectDictTypeInfo()");

        return justOrEmpty(ALL_TYPES_CACHE.get(ALL_TYPES_CACHE_KEY, DB_TYPE_GETTER)).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select all dict types
     *
     * @return
     */
    @Override
    public Mono<List<DictInfo>> selectDictInfoByTypeCode(String code) {
        LOGGER.info("Mono<List<DictInfo>> selectDictInfoByTypeCode(String code), code = {}", code);

        return isNotBlank(code)
                ?
                justOrEmpty(TYPE_CODE_DICT_CACHE.get(code, DB_DICT_GETTER)).switchIfEmpty(defer(() -> just(emptyList())))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid dict type info
     *
     * @return
     */
    @Override
    public void invalidDictTypeInfosCache() {
        LOGGER.info("void invalidDictTypeInfosCache()");

        ALL_TYPES_CACHE.invalidateAll();
    }

    /**
     * invalid dict info
     *
     * @return
     */
    @Override
    public void invalidDictInfosCache() {
        LOGGER.info("void invalidDictInfosCache()");

        ALL_TYPES_CACHE.invalidateAll();
        TYPE_CODE_DICT_CACHE.invalidateAll();
    }

}
