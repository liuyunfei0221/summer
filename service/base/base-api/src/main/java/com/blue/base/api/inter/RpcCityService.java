package com.blue.base.api.inter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * rpc city interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcCityService {

    /**
     * get city info by id
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
    CompletableFuture<CityInfo> getCityInfoMonoById(Long id);

    /**
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    List<CityInfo> selectCityInfoByStateId(Long stateId);

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    CompletableFuture<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId);

    /**
     * select city infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids);

    /**
     * select city infos mono by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids);

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
    CompletableFuture<CityRegion> getCityRegionMonoById(Long id);

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
    CompletableFuture<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids);

}
