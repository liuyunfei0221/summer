package com.blue.base.api.inter;

import com.blue.base.api.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * rpc region interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RpcRegionService {


    Optional<AreaInfo> getAreaInfoOptById(Long id);

    Optional<CityInfo> getCityInfoOptById(Long id);

    Optional<StateInfo> getStateInfoOptById(Long id);

    Optional<CountryInfo> getCountryInfoOptById(Long id);


    AreaInfo getAreaInfoById(Long id);

    CityInfo getCityInfoById(Long id);

    StateInfo getStateInfoById(Long id);

    CountryInfo getCountryInfoById(Long id);


    CompletableFuture<AreaInfo> getAreaInfoMonoById(Long id);

    CompletableFuture<CityInfo> getCityInfoMonoById(Long id);

    CompletableFuture<StateInfo> getStateInfoMonoById(Long id);

    CompletableFuture<CountryInfo> getCountryInfoMonoById(Long id);


    List<AreaInfo> selectAreaInfoByCityId(Long cityId);

    List<CityInfo> selectCityInfoByStateId(Long stateId);

    List<StateInfo> selectStateInfoByCountryId(Long countryId);

    List<CountryInfo> selectCountryInfo();


    CompletableFuture<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId);

    CompletableFuture<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId);

    CompletableFuture<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId);

    CompletableFuture<List<CountryInfo>> selectCountryInfoMono();


    Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids);

    Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids);

    Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids);

    Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids);


    CompletableFuture<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids);

    CompletableFuture<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids);

    CompletableFuture<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids);

    CompletableFuture<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids);


    AreaRegion getAreaRegionById(Long id);

    CityRegion getCityRegionById(Long id);

    StateRegion getStateRegionById(Long id);


    CompletableFuture<AreaRegion> getAreaRegionMonoById(Long id);

    CompletableFuture<CityRegion> getCityRegionMonoById(Long id);

    CompletableFuture<StateRegion> getStateRegionMonoById(Long id);


    Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids);

    Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids);

    Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids);


    CompletableFuture<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids);

    CompletableFuture<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids);

    CompletableFuture<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids);

}
