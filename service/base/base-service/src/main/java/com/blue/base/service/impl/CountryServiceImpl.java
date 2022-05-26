package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
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
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

/**
 * country service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = Loggers.getLogger(CountryServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private CountryRepository countryRepository;

    public CountryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CountryRepository countryRepository,
                              ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.countryRepository = countryRepository;

        idCountryCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        allCountriesCache = generateCache(new CaffeineConfParams(
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
     * is a country exist?
     */
    private final Consumer<CountryInsertParam> INSERT_COUNTRY_VALIDATOR = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Country probe = new Country();

        probe.setName(param.getName());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(param.getNativeName());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(param.getNumericCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(param.getCountryCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(param.getPhoneCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_PHONE_CODE_ALREADY_EXIST);
    };


    /**
     * state insert param -> state
     */
    public final Function<CountryInsertParam, Country> COUNTRY_INSERT_PARAM_2_COUNTRY_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Country country = new Country();

        country.setId(blueIdentityProcessor.generate(Country.class));
        country.setName(param.getName());
        country.setNativeName(param.getNativeName());
        country.setNumericCode(param.getNumericCode());
        country.setCountryCode(param.getCountryCode());
        country.setPhoneCode(param.getPhoneCode());
        country.setCapital(param.getCapital());
        country.setCurrency(param.getCurrency());
        country.setCurrencySymbol(param.getCurrencySymbol());
        country.setTopLevelDomain(param.getTopLevelDomain());

        //TODO
        country.setRegion(param.getRegion());
        country.setEmoji(param.getEmoji());
        country.setEmojiu(param.getEmojiu());
        country.setStatus(VALID.status);
        country.setCreateTime(stamp);
        country.setUpdateTime(stamp);

        return country;
    };

    /**
     * is a country exist?
     */
    private final Function<CountryUpdateParam, Country> UPDATE_COUNTRY_VALIDATOR_AND_ORIGIN_RETURNER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long id = param.getId();

        Country country = countryRepository.findById(id).toFuture().join();
        if (isNull(country))
            throw new BlueException(DATA_NOT_EXIST);

        Country probe = new Country();

        probe.setName(param.getName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(param.getNativeName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(param.getNumericCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(param.getCountryCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(param.getPhoneCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_PHONE_CODE_ALREADY_EXIST);

        return country;
    };

    /**
     * for country
     */
    public final BiFunction<CountryUpdateParam, Country, Boolean> UPDATE_COUNTRY_VALIDATOR = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        String nativeName = p.getNativeName();
        if (isNotBlank(nativeName) && !nativeName.equals(t.getNativeName())) {
            t.setNativeName(nativeName);
            alteration = true;
        }

        String numericCode = p.getNumericCode();
        if (isNotBlank(numericCode) && !numericCode.equals(t.getNumericCode())) {
            t.setNumericCode(numericCode);
            alteration = true;
        }

        String countryCode = p.getCountryCode();
        if (isNotBlank(countryCode) && !countryCode.equals(t.getCountryCode())) {
            t.setCountryCode(countryCode);
            alteration = true;
        }

        String phoneCode = p.getPhoneCode();
        if (isNotBlank(phoneCode) && !phoneCode.equals(t.getPhoneCode())) {
            t.setPhoneCode(phoneCode);
            alteration = true;
        }

        return alteration;
    };


    /**
     * insert country
     *
     * @param countryInsertParam
     * @return
     */
    @Override
    public Mono<CountryInfo> insertCountry(CountryInsertParam countryInsertParam) {
        LOGGER.info("Mono<CountryInfo> insertCountry(CountryInsertParam countryInsertParam), countryInsertParam = {}", countryInsertParam);

        INSERT_COUNTRY_VALIDATOR.accept(countryInsertParam);
        Country country = COUNTRY_INSERT_PARAM_2_COUNTRY_CONVERTER.apply(countryInsertParam);

        return countryRepository.insert(country)
                .map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
                    invalidCache();
                });
    }

    /**
     * update country
     *
     * @param countryUpdateParam
     * @return
     */
    @Override
    public Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam) {
        LOGGER.info("Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam), countryUpdateParam = {}", countryUpdateParam);

        Country country = UPDATE_COUNTRY_VALIDATOR_AND_ORIGIN_RETURNER.apply(countryUpdateParam);

        Boolean changed = UPDATE_COUNTRY_VALIDATOR.apply(countryUpdateParam, country);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        return countryRepository.save(country)
                .map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
                    invalidCache();
                });
    }

    /**
     * delete country
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> deleteCountry(Long id) {
        LOGGER.info("Mono<CountryInfo> deleteCountry(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Country country = countryRepository.findById(id).toFuture().join();
        if (isNull(country))
            throw new BlueException(DATA_NOT_EXIST);

        return countryRepository.delete(country)
                .then(just(COUNTRY_2_COUNTRY_INFO_CONVERTER.apply(country)))
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
                    invalidCache();
                });
    }

    /**
     * invalid chche
     */
    @Override
    public void invalidCache() {
        allCountriesCache.invalidateAll();
        idCountryCache.invalidateAll();
    }

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
        return countryRepository.findAll(Sort.by(Sort.Order.asc("name")))
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
        return just(COUNTRIES_GETTER.get()).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select country info by ids
     *
     * @return
     */
    @Override
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        return CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select country info by ids
     *
     * @return
     */
    @Override
    public Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        return just(CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * invalid country info
     *
     * @return
     */
    @Override
    public void invalidCountryInfosCache() {
        allCountriesCache.invalidateAll();
        idCountryCache.invalidateAll();
    }

}
