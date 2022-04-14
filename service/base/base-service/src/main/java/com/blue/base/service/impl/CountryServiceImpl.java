package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentities;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static com.blue.base.constant.base.ResponseElement.INVALID_PARAM;
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

/**
 * country service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    public CountryServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CountryRepository countryRepository) {
        this.countryRepository = countryRepository;

        allCountriesCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idCountryCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static final Long ALL_COUNTRIES_CACHE_ID = 0L;

    private Cache<Long, CountryInfo> idCountryCache;

    private Cache<Long, List<CountryInfo>> allCountriesCache;

    private final Function<Long, CountryInfo> DB_COUNTRY_GETTER = id ->
            this.getCountryById(id).map(COUNTRY_2_COUNTRY_INFO_CONVERTER).orElse(null);

    private final Function<Long, CountryInfo> DB_COUNTRY_GETTER_WITH_ASSERT = id ->
            this.getCountryById(id).map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

    private final Function<Long, List<CountryInfo>> DB_COUNTRIES_GETTER = ignore ->
            COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(
                    this.selectCountry().stream().sorted(Comparator.comparing(Country::getName)).collect(toList()));

    private final Function<Long, Optional<CountryInfo>> COUNTRY_OPT_BY_ID_GETTER = id ->
            ofNullable(idCountryCache.get(id, DB_COUNTRY_GETTER));

    private final Function<Long, CountryInfo> COUNTRY_BY_ID_WITH_ASSERT_GETTER = id ->
            idCountryCache.get(id, DB_COUNTRY_GETTER_WITH_ASSERT);

    private final Supplier<List<CountryInfo>> COUNTRIES_GETTER = () ->
            allCountriesCache.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRIES_GETTER);

    private final Function<List<Long>, Map<Long, CountryInfo>> CACHE_COUNTRIES_BY_IDS_GETTER = ids -> {
        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idCountryCache.getAll(l, is -> countryRepository.findAllById(l)
                                        .flatMap(c -> just(COUNTRY_2_COUNTRY_INFO_CONVERTER.apply(c)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
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
        return ofNullable(countryRepository.findById(id).toFuture().join());
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<Country> selectCountry() {
        return countryRepository.findAll(Sort.by("name"))
                .collectList().toFuture().join();
    }

    /**
     * select countries by ids
     *
     * @return
     */
    @Override
    public List<Country> selectCountryByIds(List<Long> ids) {
        if (isInvalidIdentities(ids) || ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream()
                .map(l -> countryRepository.findAllById(l)
                        .collectList().toFuture().join())
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
        return COUNTRY_OPT_BY_ID_GETTER.apply(id);
    }

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CountryInfo getCountryInfoById(Long id) {
        return COUNTRY_BY_ID_WITH_ASSERT_GETTER.apply(id);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> getCountryInfoMonoById(Long id) {
        return just(COUNTRY_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<CountryInfo> selectCountryInfo() {
        return COUNTRIES_GETTER.get();
    }

    /**
     * select all countries mono
     *
     * @return
     */
    @Override
    public Mono<List<CountryInfo>> selectCountryInfoMono() {
        return just(COUNTRIES_GETTER.get()).switchIfEmpty(just(emptyList()));
    }

    /**
     * select country infos by ids
     *
     * @return
     */
    @Override
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        return CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select country infos by ids
     *
     * @return
     */
    @Override
    public Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        return just(CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * invalid country infos
     *
     * @return
     */
    @Override
    public void invalidCountryInfosCache() {
        allCountriesCache.invalidateAll();
        idCountryCache.invalidateAll();
    }

}
