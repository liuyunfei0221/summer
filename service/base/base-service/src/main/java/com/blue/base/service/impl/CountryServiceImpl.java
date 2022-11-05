package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.CountryCondition;
import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.*;

import static com.blue.base.constant.BaseColumnName.*;
import static com.blue.base.converter.BaseModelConverters.COUNTRIES_2_COUNTRY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.COUNTRY_2_COUNTRY_INFO_CONVERTER;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_WRITE;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialIntegerElement.ONE;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
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

    private ExecutorService executorService;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private CountryRepository countryRepository;

    public CountryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService, ReactiveMongoTemplate reactiveMongoTemplate,
                              CountryRepository countryRepository, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.countryRepository = countryRepository;

        idCountryCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));

        allCountriesCache = generateCacheAsyncCache(new CaffeineConfParams(
                ONE.value, Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));
    }

    private static final Long ALL_COUNTRIES_CACHE_ID = 0L;

    private AsyncCache<Long, CountryInfo> idCountryCache;

    private AsyncCache<Long, List<CountryInfo>> allCountriesCache;

    private final BiFunction<Long, Executor, CompletableFuture<CountryInfo>> DB_COUNTRY_WITH_ASSERT_GETTER = (id, executor) -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return this.getCountryById(id).map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    };

    private final BiFunction<Long, Executor, CompletableFuture<List<CountryInfo>>> DB_COUNTRIES_GETTER = (ignore, executor) ->
            this.selectCountry().map(COUNTRIES_2_COUNTRY_INFOS_CONVERTER).toFuture();

    private final Function<Long, CompletableFuture<CountryInfo>> COUNTRY_BY_ID_WITH_ASSERT_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return idCountryCache.get(id, DB_COUNTRY_WITH_ASSERT_GETTER);
    };

    private final Supplier<CompletableFuture<List<CountryInfo>>> COUNTRIES_GETTER = () ->
            allCountriesCache.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRIES_GETTER);

    private final Function<List<Long>, CompletableFuture<Map<Long, CountryInfo>>> CACHE_COUNTRIES_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idCountryCache.getAll(ids, (is, executor) -> fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> countryRepository.findAllById(l).map(COUNTRY_2_COUNTRY_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(f -> f.collectMap(CountryInfo::getId, identity()))
                .toFuture());
    };

    private final Consumer<CountryInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Country probe = new Country();

        probe.setName(p.getName());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(p.getNativeName());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(p.getNumericCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(p.getCountryCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(p.getPhoneCode());
        if (ofNullable(countryRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
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

        Country country = countryRepository.findById(id).toFuture().join();
        if (isNull(country))
            throw new BlueException(DATA_NOT_EXIST);

        Country probe = new Country();

        probe.setName(p.getName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NAME_ALREADY_EXIST);

        probe.setName(null);
        probe.setNativeName(p.getNativeName());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NATIVE_NAME_ALREADY_EXIST);

        probe.setNativeName(null);
        probe.setNumericCode(p.getNumericCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_NUMERIC_CODE_ALREADY_EXIST);

        probe.setNumericCode(null);
        probe.setCountryCode(p.getCountryCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_CODE_ALREADY_EXIST);

        probe.setCountryCode(null);
        probe.setPhoneCode(p.getPhoneCode());
        if (ofNullable(countryRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList).stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(COUNTRY_PHONE_CODE_ALREADY_EXIST);

        return country;
    };

    public final BiConsumer<CountryUpdateParam, Country> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
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
        LOGGER.info("countryInsertParam = {}", countryInsertParam);

        INSERT_ITEM_VALIDATOR.accept(countryInsertParam);
        Country country = COUNTRY_INSERT_PARAM_2_COUNTRY_CONVERTER.apply(countryInsertParam);

        return countryRepository.insert(country)
                .map(COUNTRY_2_COUNTRY_INFO_CONVERTER);
    }

    /**
     * update country
     *
     * @param countryUpdateParam
     * @return
     */
    @Override
    public Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam) {
        LOGGER.info("countryUpdateParam = {}", countryUpdateParam);

        Country country = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(countryUpdateParam);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(countryUpdateParam, country);

        return countryRepository.save(country)
                .map(COUNTRY_2_COUNTRY_INFO_CONVERTER);
    }

    /**
     * delete country
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> deleteCountry(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return countryRepository.findById(id)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(country -> {
                    State probe = new State();
                    probe.setCountryId(id);

                    Query query = new Query();
                    query.addCriteria(byExample(probe));

                    return reactiveMongoTemplate.count(query, State.class)
                            .flatMap(stateCount ->
                                    stateCount <= 0L ?
                                            countryRepository.delete(country)
                                            :
                                            error(new BlueException(REGION_DATA_STILL_USED))
                            )
                            .then(just(COUNTRY_2_COUNTRY_INFO_CONVERTER.apply(country)));
                });
    }

    /**
     * invalid cache
     */
    @Override
    public void invalidCache() {
        allCountriesCache.synchronous().invalidateAll();
        idCountryCache.synchronous().invalidateAll();
    }

    /**
     * get country by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Country> getCountryById(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return countryRepository.findById(id);
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public Mono<List<Country>> selectCountry() {
        return countryRepository.findAll(by(Sort.Order.asc(NAME.name)))
                .collectList();
    }

    /**
     * select countries by ids
     *
     * @return
     */
    @Override
    public Mono<List<Country>> selectCountryByIds(List<Long> ids) {
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> countryRepository.findAllById(l))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> getCountryInfoById(Long id) {
        return fromFuture(COUNTRY_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select all countries mono
     *
     * @return
     */
    @Override
    public Mono<List<CountryInfo>> selectCountryInfo() {
        return fromFuture(COUNTRIES_GETTER.get()).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select country info by ids
     *
     * @return
     */
    @Override
    public Mono<Map<Long, CountryInfo>> selectCountryInfoByIds(List<Long> ids) {
        return fromFuture(CACHE_COUNTRIES_BY_IDS_GETTER.apply(ids));
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
    public Mono<List<Country>> selectCountryByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Country.class).collectList();
    }

    /**
     * count country by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countCountryByQuery(Query query) {
        LOGGER.info("query = {}", query);
        return reactiveMongoTemplate.count(query, Country.class);
    }

    /**
     * select country info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<CountryInfo>> selectCountryPageByPageAndCondition(PageModelRequest<CountryCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectCountryByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countCountryByQuery(query)
        ).flatMap(tuple2 -> {
            List<Country> countries = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(countries) ?
                    just(new PageModelResponse<>(COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(countries), count))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
