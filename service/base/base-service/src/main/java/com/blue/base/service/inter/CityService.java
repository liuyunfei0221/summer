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
@SuppressWarnings({"JavaDoc", "unused"})
public interface CityService {

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    Optional<City> getCityById(Long id);

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    List<City> selectCityByStateId(Long stateId);

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    List<City> selectCityByIds(List<Long> ids);

    /**
     * get city info opt by country id
     *
     * @param id
     * @return
     */
    Optional<CityInfo> getCityInfoOptById(Long id);

    /**
     * get city info by country id with assert
     *
     * @param id
     * @return
     */
    CityInfo getCityInfoById(Long id);

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    Mono<CityInfo> getCityInfoMonoById(Long id);

    /**
     * select states mono by state id
     *
     * @param stateId
     * @return
     */
    List<CityInfo> selectCityInfoByStateId(Long stateId);

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId);

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    List<CityInfo> selectCityInfoByIds(List<Long> ids);

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoMonoByIds(List<Long> ids);

    /**
     * invalid city infos
     *
     * @return
     */
    void invalidCityInfosCache();

}
