package com.blue.base.service.inter;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.base.model.*;
import reactor.core.publisher.Mono;

/**
 * config country,state,city,area service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RegionControlService {

    /**
     * insert country
     *
     * @param countryInsertParam
     * @return
     */
    Mono<CountryInfo> insertCountry(CountryInsertParam countryInsertParam);

    /**
     * update country
     *
     * @param countryUpdateParam
     * @return
     */
    Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam);

    /**
     * delete country
     *
     * @param id
     * @return
     */
    Mono<CountryInfo> deleteCountry(Long id);

    /**
     * insert state
     *
     * @param stateInsertParam
     * @return
     */
    Mono<StateInfo> insertState(StateInsertParam stateInsertParam);

    /**
     * update state
     *
     * @param stateUpdateParam
     * @return
     */
    Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam);

    /**
     * delete state
     *
     * @param id
     * @return
     */
    Mono<StateInfo> deleteState(Long id);

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
     * invalid all chche
     *
     * @return
     */
    Mono<Void> invalidAllCache();

}
