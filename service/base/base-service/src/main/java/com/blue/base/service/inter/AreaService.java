package com.blue.base.service.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.model.AreaCondition;
import com.blue.base.model.AreaInsertParam;
import com.blue.base.model.AreaUpdateParam;
import com.blue.base.repository.entity.Area;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * area service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AreaService {

    /**
     * insert area
     *
     * @param areaInsertParam
     * @return
     */
    Mono<AreaInfo> insertArea(AreaInsertParam areaInsertParam);

    /**
     * update area
     *
     * @param areaUpdateParam
     * @return
     */
    Mono<AreaInfo> updateArea(AreaUpdateParam areaUpdateParam);

    /**
     * delete area
     *
     * @param id
     * @return
     */
    Mono<AreaInfo> deleteArea(Long id);

    /**
     * invalid cache
     */
    void invalidCache();

    /**
     * get area by id
     *
     * @param id
     * @return
     */
    Mono<Area> getAreaById(Long id);

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    Mono<List<Area>> selectAreaByCityId(Long cityId);

    /**
     * select area by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Area>> selectAreaByIds(List<Long> ids);

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    Mono<AreaInfo> getAreaInfoById(Long id);

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId);

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, AreaInfo>> selectAreaInfoByIds(List<Long> ids);

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    Mono<AreaRegion> getAreaRegionById(Long id);

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, AreaRegion>> selectAreaRegionByIds(List<Long> ids);

    /**
     * select area by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<Area>> selectAreaByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count area by query
     *
     * @param query
     * @return
     */
    Mono<Long> countAreaByQuery(Query query);

    /**
     * select area info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AreaInfo>> selectAreaPageByPageAndCondition(PageModelRequest<AreaCondition> pageModelRequest);

}
