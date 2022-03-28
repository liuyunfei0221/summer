package com.blue.base.api.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * rpc area interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAreaService {

    /**
     * get area info opt by id
     *
     * @param id
     * @return
     */
    Optional<AreaInfo> getAreaInfoOptById(Long id);

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    AreaInfo getAreaInfoById(Long id);

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    CompletableFuture<AreaInfo> getAreaInfoMonoById(Long id);

    /**
     * select area infos by city id
     *
     * @param cityId
     * @return
     */
    List<AreaInfo> selectAreaInfoByCityId(Long cityId);

    /**
     * select area infos mono by city id
     *
     * @param cityId
     * @return
     */
    CompletableFuture<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId);

    /**
     * select area infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids);

    /**
     * select area infos mono by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids);

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    AreaRegion getAreaRegionById(Long id);

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    CompletableFuture<AreaRegion> getAreaRegionMonoById(Long id);

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids);

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids);

}
