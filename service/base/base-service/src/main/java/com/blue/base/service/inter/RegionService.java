package com.blue.base.service.inter;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateRegion;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * region service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RegionService {

    AreaRegion getAreaRegionById(Long id);

    CityRegion getCityRegionById(Long id);

    StateRegion getStateRegionById(Long id);

    CountryInfo getCountryInfoById(Long id);


    Mono<AreaRegion> getAreaRegionMonoById(Long id);

    Mono<CityRegion> getCityRegionMonoById(Long id);

    Mono<StateRegion> getStateRegionMonoById(Long id);

    Mono<CountryInfo> getCountryInfoMonoById(Long id);


    Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids);

    Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids);

    Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids);

    Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids);


    Mono<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids);

    Mono<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids);

    Mono<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids);

    Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids);

}
