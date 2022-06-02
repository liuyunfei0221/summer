package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.CountryCondition;
import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
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
import static com.blue.base.constant.ColumnName.NAME;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = Loggers.getLogger(CountryServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private CountryRepository countryRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CountryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CountryRepository countryRepository, ReactiveMongoTemplate reactiveMongoTemplate,
                              ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.countryRepository = countryRepository;

        idCountryCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        allCountriesCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), SECONDS),
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

    private static final String NAME_COLUMN_NAME = "name";
    private static final String NATIVE_NAME_COLUMN_NAME = "nativeName";
    private static final String NUMERIC_CODE_COLUMN_NAME = "numericCode";
    private static final String COUNTRY_CODE_COLUMN_NAME = "countryCode";
    private static final String PHONE_CODE_COLUMN_NAME = "phoneCode";
    private static final String CAPITAL_COLUMN_NAME = "capital";
    private static final String TOP_LEVEL_DOMAIN_COLUMN_NAME = "topLevelDomain";
    private static final String REGION_COLUMN_NAME = "region";

    private static final Function<CountryCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        Country probe = new Country();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getCurrency()).ifPresent(probe::setCurrency);
        ofNullable(condition.getCurrencySymbol()).ifPresent(probe::setCurrencySymbol);
        ofNullable(condition.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(condition.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME_COLUMN_NAME).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getNativeNameLike()).ifPresent(nativeNameLike ->
                query.addCriteria(where(NATIVE_NAME_COLUMN_NAME).regex(compile(PREFIX.element + nativeNameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getNumericCodeLike()).ifPresent(numericCodeLike ->
                query.addCriteria(where(NUMERIC_CODE_COLUMN_NAME).regex(compile(PREFIX.element + numericCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getCountryCodeLike()).ifPresent(countryCodeLike ->
                query.addCriteria(where(COUNTRY_CODE_COLUMN_NAME).regex(compile(PREFIX.element + countryCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getPhoneCodeLike()).ifPresent(phoneCodeLike ->
                query.addCriteria(where(PHONE_CODE_COLUMN_NAME).regex(compile(PREFIX.element + phoneCodeLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getCapitalLike()).ifPresent(capitalLike ->
                query.addCriteria(where(CAPITAL_COLUMN_NAME).regex(compile(PREFIX.element + capitalLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getTopLevelDomainLike()).ifPresent(topLevelDomainLike ->
                query.addCriteria(where(TOP_LEVEL_DOMAIN_COLUMN_NAME).regex(compile(PREFIX.element + topLevelDomainLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getRegionLike()).ifPresent(regionLike ->
                query.addCriteria(where(REGION_COLUMN_NAME).regex(compile(PREFIX.element + regionLike + SUFFIX.element, CASE_INSENSITIVE))));

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
        return countryRepository.findAll(by(Sort.Order.asc(NAME.name)))
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

        return reactiveMongoTemplate.find(listQuery, Country.class).collectList();
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
        return reactiveMongoTemplate.count(query, Country.class);
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

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

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
