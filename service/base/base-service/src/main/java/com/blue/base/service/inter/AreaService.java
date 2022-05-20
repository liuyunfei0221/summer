package com.blue.base.service.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.repository.entity.Area;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * area service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AreaService {

    /**
     * get area by id
     *
     * @param id
     * @return
     */
    Optional<Area> getAreaById(Long id);

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    List<Area> selectAreaByCityId(Long cityId);

    /**
     * select area by ids
     *
     * @param ids
     * @return
     */
    List<Area> selectAreaByIds(List<Long> ids);

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
    Mono<AreaInfo> getAreaInfoMonoById(Long id);

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    List<AreaInfo> selectAreaInfoByCityId(Long cityId);

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId);

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids);

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids);

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
    Mono<AreaRegion> getAreaRegionMonoById(Long id);

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
    Mono<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids);

    /**
     * invalid area info
     *
     * @return
     */
    void invalidAreaInfosCache();

}
