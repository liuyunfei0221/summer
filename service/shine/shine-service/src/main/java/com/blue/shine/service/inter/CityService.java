package com.blue.shine.service.inter;

import com.blue.base.api.model.CityRegion;

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
    CityRegion getCityRegionById(Long id);

}
