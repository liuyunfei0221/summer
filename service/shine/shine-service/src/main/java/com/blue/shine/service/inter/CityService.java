package com.blue.shine.service.inter;

import com.blue.base.api.model.CityRegion;
import reactor.core.publisher.Mono;

/**
 * city service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface CityService {

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    Mono<CityRegion> getCityRegionById(Long id);

}
