package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.CountryCondition;
import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.BaseColumnName.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.base.converter.BaseModelConverters.COUNTRIES_2_COUNTRY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.COUNTRY_2_COUNTRY_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;

/**
 * country service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = Loggers.getLogger(CountryServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private CountryRepository countryRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler scheduler;

    public CountryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CountryRepository countryRepository, ReactiveMongoTemplate reactiveMongoTemplate,
                              Scheduler scheduler, ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.countryRepository = countryRepository;

        idCountryCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));

        allCountriesCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
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
            COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(this.selectCountry());

    private final Function<Long, Optional<CountryInfo>> COUNTRY_OPT_BY_ID_GETTER = id ->
            ofNullable(idCountryCache.get(id, DB_COUNTRY_GETTER));

    private final Function<Long, CountryInfo> COUNTRY_BY_ID_WITH_ASSERT_GETTER = id ->
            idCountryCache.get(id, DB_COUNTRY_GETTER_WITH_ASSERT);

    private final Supplier<List<CountryInfo>> COUNTRIES_GETTER = () ->
            allCountriesCache.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRIES_GETTER);

    private final Function<List<Long>, Map<Long, CountryInfo>> CACHE_COUNTRIES_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return emptyMap();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idCountryCache.getAll(l, is -> countryRepository.findAllById(l)
                                        .publishOn(scheduler)
                                        .flatMap(c -> just(COUNTRY_2_COUNTRY_INFO_CONVERTER.apply(c)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
                                        .collect(toMap(CountryInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    private final Consumer<CountryInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Country probe = new Country();

        probe.setName(p.getName());
        if (ofNullable(countryRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(p.getNativeName());
        if (ofNullable(countryRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(p.getNumericCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(p.getCountryCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(p.getPhoneCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_PHONE_CODE_ALREADY_EXIST);
    };

    public final Function<CountryInsertParam, Country> COUNTRY_INSERT_PARAM_2_COUNTRY_CONVERTER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Country country = new Country();

        country.setId(blueIdentityProcessor.generate(Country.class));
        country.setName(p.getName());
        country.setNativeName(p.getNativeName());
        country.setNumericCode(p.getNumericCode());
        country.setCountryCode(p.getCountryCode());
        country.setPhoneCode(p.getPhoneCode());
        country.setCapital(p.getCapital());
        country.setCurrency(p.getCurrency());
        country.setCurrencySymbol(p.getCurrencySymbol());
        country.setTopLevelDomain(p.getTopLevelDomain());

        //TODO
        country.setRegion(p.getRegion());
        country.setEmoji(p.getEmoji());
        country.setEmojiu(p.getEmojiu());
        country.setStatus(VALID.status);
        country.setCreateTime(stamp);
        country.setUpdateTime(stamp);

        return country;
    };

    private final Function<CountryUpdateParam, Country> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        Country country = countryRepository.findById(id).publishOn(scheduler).toFuture().join();
        if (isNull(country))
            throw new BlueException(DATA_NOT_EXIST);

        Country probe = new Country();

        probe.setName(p.getName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(p.getNativeName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(p.getNumericCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(p.getCountryCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(p.getPhoneCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_PHONE_CODE_ALREADY_EXIST);

        return country;
    };

    public final BiFunction<CountryUpdateParam, Country, Boolean> UPDATE_ITEM_VALIDATOR = (p, t) -> {
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

    private static final Function<CountryCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null)
            return query;

        Country probe = new Country();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getCurrency()).ifPresent(probe::setCurrency);
        ofNullable(c.getCurrencySymbol()).ifPresent(probe::setCurrencySymbol);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getNativeNameLike()).ifPresent(nativeNameLike ->
                query.addCriteria(where(NATIVE_NAME.name).regex(compile(PREFIX.element + nativeNameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getNumericCodeLike()).ifPresent(numericCodeLike ->
                query.addCriteria(where(NUMERIC_CODE.name).regex(compile(PREFIX.element + numericCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getCountryCodeLike()).ifPresent(countryCodeLike ->
                query.addCriteria(where(COUNTRY_CODE.name).regex(compile(PREFIX.element + countryCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getPhoneCodeLike()).ifPresent(phoneCodeLike ->
                query.addCriteria(where(PHONE_CODE.name).regex(compile(PREFIX.element + phoneCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getCapitalLike()).ifPresent(capitalLike ->
                query.addCriteria(where(CAPITAL.name).regex(compile(PREFIX.element + capitalLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getTopLevelDomainLike()).ifPresent(topLevelDomainLike ->
                query.addCriteria(where(TOP_LEVEL_DOMAIN.name).regex(compile(PREFIX.element + topLevelDomainLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getRegionLike()).ifPresent(regionLike ->
                query.addCriteria(where(REGION.name).regex(compile(PREFIX.element + regionLike + SUFFIX.element, CASE_INSENSITIVE))));

        query.with(by(Sort.Order.asc(NAME.name)));

        return query;
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

        INSERT_ITEM_VALIDATOR.accept(countryInsertParam);
        Country country = COUNTRY_INSERT_PARAM_2_COUNTRY_CONVERTER.apply(countryInsertParam);

        return countryRepository.insert(country)
                .publishOn(scheduler)
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

        Country country = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(countryUpdateParam);

        Boolean changed = UPDATE_ITEM_VALIDATOR.apply(countryUpdateParam, country);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        country.setUpdateTime(TIME_STAMP_GETTER.get());
        return countryRepository.save(country)
                .publishOn(scheduler)
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

        return countryRepository.findById(id)
                .publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(country -> {
                    State probe = new State();
                    probe.setCountryId(id);

                    Query query = new Query();
                    query.addCriteria(byExample(probe));

                    return reactiveMongoTemplate.count(query, State.class)
                            .publishOn(scheduler)
                            .flatMap(stateCount ->
                                    stateCount <= 0L ?
                                            countryRepository.delete(country)
                                            :
                                            error(new BlueException(REGION_DATA_STILL_USED))
                            )
                            .then(just(COUNTRY_2_COUNTRY_INFO_CONVERTER.apply(country)))
                            .doOnSuccess(ci -> {
                                LOGGER.info("ci = {}", ci);
                                invalidCache();
                            });
                });
    }

    /**
     * invalid cache
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
        return ofNullable(countryRepository.findById(id).publishOn(scheduler).toFuture().join());
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<Country> selectCountry() {
        return countryRepository.findAll(by(Sort.Order.asc(NAME.name)))
                .publishOn(scheduler).collectList().toFuture().join();
    }

    /**
     * select countries by ids
     *
     * @return
     */
    @Override
    public List<Country> selectCountryByIds(List<Long> ids) {
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream()
                .map(l -> countryRepository.findAllById(l)
                        .publishOn(scheduler).collectList().toFuture().join())
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
     * select country by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<Country>> selectCountryMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Country>> selectCountryMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Country.class).publishOn(scheduler).collectList();
    }

    /**
     * count country by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countCountryMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countCountryMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Country.class).publishOn(scheduler);
    }

    /**
     * select country info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<CountryInfo>> selectCountryPageMonoByPageAndCondition(PageModelRequest<CountryCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<CountryInfo>> selectCountryPageMonoByPageAndCondition(PageModelRequest<CountryCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectCountryMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countCountryMonoByQuery(query)
        ).flatMap(tuple2 -> {
            List<Country> countries = tuple2.getT1();
            return isNotEmpty(countries) ?
                    just(new PageModelResponse<>(COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(countries), tuple2.getT2()))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}
