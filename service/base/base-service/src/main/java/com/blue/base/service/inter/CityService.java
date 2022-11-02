package com.blue.base.service.inter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.model.CityCondition;
import com.blue.base.model.CityInsertParam;
import com.blue.base.model.CityUpdateParam;
import com.blue.base.repository.entity.City;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
     * invalid cache
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
    Mono<City> getCityById(Long id);

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<City>> selectCityByStateId(Long stateId);

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    Mono<List<City>> selectCityByIds(List<Long> ids);

    /**
     * get city info opt by id
     *
     * @param id
     * @return
     */
    Mono<CityInfo> getCityInfoById(Long id);

    /**
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId);

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CityInfo>> selectCityInfoByIds(List<Long> ids);

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    Mono<CityRegion> getCityRegionById(Long id);

    /**
     * get city regions by id
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CityRegion>> selectCityRegionByIds(List<Long> ids);

    /**
     * select city by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<City>> selectCityByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count city by query
     *
     * @param query
     * @return
     */
    Mono<Long> countCityByQuery(Query query);

    /**
     * select city info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<CityInfo>> selectCityPageByPageAndCondition(PageModelRequest<CityCondition> pageModelRequest);

}
