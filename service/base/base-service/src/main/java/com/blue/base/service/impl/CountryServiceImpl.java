package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.mapper.CountryMapper;
import com.blue.base.service.inter.CountryService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.converter.BaseModelConverters.COUNTRIES_2_COUNTRY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.COUNTRY_2_COUNTRY_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * country service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = getLogger(CountryServiceImpl.class);

    private CountryMapper countryMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CountryServiceImpl(ExecutorService executorService, CountryMapper countryMapper, AreaCaffeineDeploy areaCaffeineDeploy) {
        this.countryMapper = countryMapper;

        ALL_COUNTRIES_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        ID_COUNTRY_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static final Long ALL_COUNTRIES_CACHE_ID = 0L;

    private static Cache<Long, List<CountryInfo>> ALL_COUNTRIES_CACHE;

    private static Cache<Long, CountryInfo> ID_COUNTRY_CACHE;


    private final Function<Long, List<CountryInfo>> DB_COUNTRIES_GETTER = ignore -> {
        List<Country> countries = this.selectCountry();

        LOGGER.info("DB_COUNTRY_GETTER, countries = {}", countries);
        return COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(
                countries.stream().sorted(Comparator.comparing(Country::getCountryCode)).collect(toList()));
    };

    private final Function<Long, CountryInfo> DB_COUNTRY_GETTER = id -> {
        LOGGER.info("DB_COUNTRY_GETTER, id = {}", id);
        return this.getCountryById(id).map(COUNTRY_2_COUNTRY_INFO_CONVERTER).orElse(null);
    };

    private final Function<Long, CountryInfo> DB_COUNTRY_GETTER_WITH_ASSERT = id -> {
        LOGGER.info("DB_COUNTRY_GETTER_WITH_ASSERT, id = {}", id);
        return this.getCountryById(id).map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final Function<List<Long>, Map<Long, CountryInfo>> CACHE_COUNTRIES_BY_IDS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, List<CountryInfo>> COUNTRIES_BY_IDS_GETTER_WITH_CACHE, ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        ID_COUNTRY_CACHE.getAll(l, is -> countryMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                                        .collect(toMap(CountryInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    /**
     * get country by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Country> getCountryById(Long id) {
        LOGGER.info("Optional<Country> getCountryById(Long id), id = {}", id);

        if (isValidIdentity(id))
            return ofNullable(countryMapper.selectByPrimaryKey(id));

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<Country> selectCountry() {
        LOGGER.info("List<Country> selectCountry()");

        return countryMapper.select();
    }

    /**
     * select countries by ids
     *
     * @return
     */
    @Override
    public List<Country> selectCountryByIds(List<Long> ids) {
        LOGGER.info("List<Country> selectCountryByIds(List<Long> ids), ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyList();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(countryMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * get country info by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<CountryInfo> getCountryInfoOptById(Long id) {
        return ofNullable(ID_COUNTRY_CACHE.get(id, DB_COUNTRY_GETTER));
    }

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CountryInfo getCountryInfoById(Long id) {
        return ID_COUNTRY_CACHE.get(id, DB_COUNTRY_GETTER_WITH_ASSERT);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> getCountryInfoMonoById(Long id) {
        return just(ID_COUNTRY_CACHE.get(id, DB_COUNTRY_GETTER_WITH_ASSERT));
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<CountryInfo> selectCountryInfo() {
        return ALL_COUNTRIES_CACHE.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRIES_GETTER);
    }

    /**
     * select all countries mono
     *
     * @return
     */
    @Override
    public Mono<List<CountryInfo>> selectCountryInfoMono() {
        return just(ALL_COUNTRIES_CACHE.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRIES_GETTER)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select country infos by ids
     *
     * @return
     */
    @Override
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        LOGGER.info("List<CountryInfo> selectCountryInfoByIds(List<Long> ids), ids = {}", ids);

        return CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select country infos by ids
     *
     * @return
     */
    @Override
    public Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids), ids = {}", ids);

        return just(CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * invalid country infos
     *
     * @return
     */
    @Override
    public void invalidCountryInfosCache() {
        LOGGER.info("void invalidCountryInfosCache()");

        ALL_COUNTRIES_CACHE.invalidateAll();
        ID_COUNTRY_CACHE.invalidateAll();
    }

}
