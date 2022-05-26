package com.blue.base.service.inter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.model.CityInsertParam;
import com.blue.base.model.CityUpdateParam;
import com.blue.base.repository.entity.City;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * city service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface CityService {

    /**
     * insert city
     *
     * @param cityInsertParam
     * @return
     */
    Mono<CityInfo> insertCity(CityInsertParam cityInsertParam);

    /**
     * update city
     *
     * @param cityUpdateParam
     * @return
     */
    Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam);

    /**
     * delete city
     *
     * @param id
     * @return
     */
    Mono<CityInfo> deleteCity(Long id);

    /**
     * invalid chche
     */
    void invalidCache();

    /**
     * a city's stateId was changed
     *
     * @param countryId
     * @param stateId
     * @param cityId
     * @return
     */
    Mono<Long> updateCountryIdAndStateIdOfAreaByCityId(Long countryId, Long stateId, Long cityId);

    /**
     * get city by id
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
     * get city info opt by id
     *
     * @param id
     * @return
     */
    Optional<CityInfo> getCityInfoOptById(Long id);

    /**
     * get city info by id with assert
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
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    List<CityInfo> selectCityInfoByStateId(Long stateId);

    /**
     * select city info mono by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId);

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids);

    /**
     * select city info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids);

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    CityRegion getCityRegionById(Long id);

    /**
     * get city region mono by id
     *
     * @param id
     * @return
     */
    Mono<CityRegion> getCityRegionMonoById(Long id);

    /**
     * get city regions by id
     *
     * @param ids
     * @return
     */
    Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids);

    /**
     * get city regions mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids);

    /**
     * invalid city info
     *
     * @return
     */
    void invalidCityInfosCache();

}
