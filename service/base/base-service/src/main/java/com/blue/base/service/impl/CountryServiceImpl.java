package com.blue.base.service.impl;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.mapper.CountryMapper;
import com.blue.base.service.inter.CountryService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.converter.BaseModelConverters.COUNTRIES_2_COUNTRY_INFOS_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * country service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = getLogger(CountryServiceImpl.class);

    private final CountryMapper countryMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CountryServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CountryMapper countryMapper) {
        this.countryMapper = countryMapper;

        ALL_COUNTRIES_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService));
    }

    private static final Long ALL_COUNTRIES_CACHE_ID = 1L;

    private static Cache<Long, List<CountryInfo>> ALL_COUNTRIES_CACHE;

    private final Function<Long, List<CountryInfo>> DB_COUNTRY_GETTER = id -> {
        List<Country> countries = this.selectCountry();

        LOGGER.info("DB_COUNTRY_GETTER, id = {}, countries = {}", id, countries);
        return COUNTRIES_2_COUNTRY_INFOS_CONVERTER.apply(countries);
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

        return ofNullable(countryMapper.selectByPrimaryKey(id));
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<Country> selectCountry() {
        LOGGER.info(" List<Country> selectCountry()");

        return countryMapper.select();
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public Mono<List<CountryInfo>> selectCountryInfo() {
        return justOrEmpty(ALL_COUNTRIES_CACHE.get(ALL_COUNTRIES_CACHE_ID, DB_COUNTRY_GETTER)).switchIfEmpty(just(emptyList()));
    }

    /**
     * expire country infos
     *
     * @return
     */
    @Override
    public void invalidCountryInfosCache() {
        ALL_COUNTRIES_CACHE.invalidateAll();
    }
}
