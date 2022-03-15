package com.blue.base.service.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.repository.entity.Area;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * area service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AreaService {

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    Optional<Area> getAreaById(Long id);

    /**
     * select area by country id
     *
     * @param countryId
     * @return
     */
    List<Area> selectAreaByCountryId(Long countryId);

    /**
     * select area by state id
     *
     * @param stateId
     * @return
     */
    List<Area> selectAreaByStateId(Long stateId);

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    List<Area> selectAreaByCityId(Long cityId);

    /**
     * select all areas
     *
     * @return
     */
    List<Area> selectArea();

    /**
     * select areas by city id
     *
     * @param cityId
     * @return
     */
    Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId);

    /**
     * invalid city infos
     *
     * @return
     */
    void invalidAreaInfosCache();

}
