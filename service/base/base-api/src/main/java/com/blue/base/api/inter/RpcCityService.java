package com.blue.base.api.inter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;

import java.util.List;
import java.util.Map;
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
    CompletableFuture<CityInfo> getCityInfoById(Long id);

    /**
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    CompletableFuture<List<CityInfo>> selectCityInfoByStateId(Long stateId);

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, CityInfo>> selectCityInfoByIds(List<Long> ids);

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    CompletableFuture<CityRegion> getCityRegionById(Long id);

    /**
     * get city regions by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, CityRegion>> selectCityRegionByIds(List<Long> ids);

}
