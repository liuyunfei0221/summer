package com.blue.base.api.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * rpc area interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAreaService {

    /**
     * get area info by id
     *
     * @param id
     * @return
     */
    CompletableFuture<AreaInfo> getAreaInfoById(Long id);

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    CompletableFuture<List<AreaInfo>> selectAreaInfoByCityId(Long cityId);

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, AreaInfo>> selectAreaInfoByIds(List<Long> ids);

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    CompletableFuture<AreaRegion> getAreaRegionById(Long id);

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, AreaRegion>> selectAreaRegionByIds(List<Long> ids);

}
