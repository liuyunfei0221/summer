package com.blue.base.service.impl;

import com.blue.base.api.model.*;
import com.blue.base.model.exps.BlueException;
import com.blue.base.service.inter.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static reactor.core.publisher.Mono.*;

/**
 * region service impl
 *
 * @author DarkBlue
 */
@Service
public class RegionServiceImpl implements RegionService {

    private AreaService areaService;

    private CityService cityService;

    private StateService stateService;

    private CountryService countryService;

    public RegionServiceImpl(AreaService areaService, CityService cityService, StateService stateService, CountryService countryService) {
        this.areaService = areaService;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
    }

    private final Function<Long, AreaRegion> AREA_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            AreaInfo areaInfo = areaService.getAreaInfoById(id);
            return new AreaRegion(countryService.getCountryInfoById(areaInfo.getCountryId()), stateService.getStateInfoById(areaInfo.getStateId()),
                    cityService.getCityInfoById(areaInfo.getCityId()), areaInfo);
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, Mono<AreaRegion>> AREA_REGION_MONO_GETTER = id ->
            isValidIdentity(id) ?
                    areaService.getAreaInfoMonoById(id)
                            .flatMap(areaInfo ->
                                    zip(
                                            countryService.getCountryInfoMonoById(areaInfo.getCountryId()),
                                            stateService.getStateInfoMonoById(areaInfo.getStateId()),
                                            cityService.getCityInfoMonoById(areaInfo.getCityId())
                                    ).flatMap(tuple3 ->
                                            just(new AreaRegion(tuple3.getT1(), tuple3.getT2(), tuple3.getT3(), areaInfo))))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));


    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            CityInfo cityInfo = cityService.getCityInfoById(id);
            return new CityRegion(countryService.getCountryInfoById(cityInfo.getCountryId()), stateService.getStateInfoById(cityInfo.getStateId()), cityInfo);
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, Mono<CityRegion>> CITY_REGION_MONO_GETTER = id ->
            isValidIdentity(id) ?
                    cityService.getCityInfoMonoById(id)
                            .flatMap(cityInfo ->
                                    zip(
                                            countryService.getCountryInfoMonoById(cityInfo.getCountryId()),
                                            stateService.getStateInfoMonoById(cityInfo.getStateId())
                                    ).flatMap(tuple2 ->
                                            just(new CityRegion(tuple2.getT1(), tuple2.getT2(), cityInfo))))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));


    private final Function<Long, StateRegion> STATE_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            StateInfo stateInfo = stateService.getStateInfoById(id);
            return new StateRegion(countryService.getCountryInfoById(stateInfo.getCountryId()), stateInfo);
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, Mono<StateRegion>> STATE_REGION_MONO_GETTER = id ->
            isValidIdentity(id) ?
                    stateService.getStateInfoMonoById(id)
                            .flatMap(stateInfo ->
                                    countryService.getCountryInfoMonoById(stateInfo.getCountryId())
                                            .flatMap(countryInfo -> just(new StateRegion(countryInfo, stateInfo))))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));


    @Override
    public AreaRegion getAreaRegionById(Long id) {
        return AREA_REGION_GETTER.apply(id);
    }

    @Override
    public CityRegion getCityRegionById(Long id) {
        return CITY_REGION_GETTER.apply(id);
    }

    @Override
    public StateRegion getStateRegionById(Long id) {
        return STATE_REGION_GETTER.apply(id);
    }

    @Override
    public CountryInfo getCountryInfoById(Long id) {
        return countryService.getCountryInfoById(id);
    }


    @Override
    public Mono<AreaRegion> getAreaRegionMonoById(Long id) {
        return AREA_REGION_MONO_GETTER.apply(id);
    }

    @Override
    public Mono<CityRegion> getCityRegionMonoById(Long id) {
        return CITY_REGION_MONO_GETTER.apply(id);
    }

    @Override
    public Mono<StateRegion> getStateRegionMonoById(Long id) {
        return STATE_REGION_MONO_GETTER.apply(id);
    }

    @Override
    public Mono<CountryInfo> getCountryInfoMonoById(Long id) {
        return countryService.getCountryInfoMonoById(id);
    }


    @Override
    public Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        return countryService.selectCountryInfoByIds(ids);
    }


    @Override
    public Mono<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Mono<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Mono<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        return countryService.selectCountryInfoMonoByIds(ids);
    }

}
