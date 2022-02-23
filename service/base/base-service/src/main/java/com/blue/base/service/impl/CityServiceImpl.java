package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.mapper.CityMapper;
import com.blue.base.service.inter.CityService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.BlueCheck.isInvalidIdentity;
import static com.blue.base.common.base.BlueCheck.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * city service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = getLogger(CityServiceImpl.class);

    private final CityMapper cityMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CityServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CityMapper cityMapper) {
        this.cityMapper = cityMapper;

        STATE_ID_CITY_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<CityInfo>> STATE_ID_CITY_CACHE;

    private final Function<Long, List<CityInfo>> DB_CITY_GETTER = sid -> {
        List<City> cities = this.selectCityByStateId(sid);

        LOGGER.info("DB_CITY_GETTER, sid = {}, cities = {}", sid, cities);
        return CITIES_2_CITY_INFOS_CONVERTER.apply(
                cities.stream().sorted(Comparator.comparing(City::getName)).collect(toList()));
    };

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<City> getCityById(Long id) {
        LOGGER.info("Optional<City> getCityById(Long id), id = {}", id);

        return ofNullable(cityMapper.selectByPrimaryKey(id));
    }

    /**
     * select city by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<City> selectCityByCountryId(Long countryId) {
        LOGGER.info("List<City> selectCityByCountryId(Long countryId), countryId = {}", countryId);

        if (isInvalidIdentity(countryId))
            throw new BlueException(INVALID_IDENTITY);

        return cityMapper.selectByCountryId(countryId);
    }

    /**
     * select city by stateId id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<City> selectCityByStateId(Long stateId) {
        LOGGER.info("List<City> selectCityByStateId(Long stateId), stateId = {}", stateId);

        if (isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        return cityMapper.selectByStateId(stateId);
    }

    /**
     * select all city
     *
     * @return
     */
    @Override
    public List<City> selectCity() {
        LOGGER.info("List<City> selectCity()");

        return cityMapper.select();
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId) {
        LOGGER.info("Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId), stateId = {}", stateId);

        return isValidIdentity(stateId)
                ?
                justOrEmpty(STATE_ID_CITY_CACHE.get(stateId, DB_CITY_GETTER)).switchIfEmpty(just(emptyList()))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid city infos
     *
     * @return
     */
    @Override
    public void invalidCityInfosCache() {
        LOGGER.info("void invalidCityInfosCache()");

        STATE_ID_CITY_CACHE.invalidateAll();
    }

}
