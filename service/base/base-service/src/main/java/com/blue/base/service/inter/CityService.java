package com.blue.base.service.inter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.repository.entity.City;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * city service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CityService {

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    Optional<City> getCityById(Long id);

    /**
     * select city by country id
     *
     * @param countryId
     * @return
     */
    List<City> selectCityByCountryId(Long countryId);

    /**
     * select city by stateId id
     *
     * @param stateId
     * @return
     */
    List<City> selectCityByStateId(Long stateId);

    /**
     * select all city
     *
     * @return
     */
    List<City> selectCity();

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId);

    /**
     * expire city infos
     *
     * @return
     */
    void invalidCityInfosCache();

}
