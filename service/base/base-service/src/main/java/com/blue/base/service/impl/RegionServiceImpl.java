package com.blue.base.service.impl;

import com.blue.base.api.model.*;
import com.blue.base.model.exps.BlueException;
import com.blue.base.service.inter.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isValidIdentities;
import static com.blue.base.common.base.BlueChecker.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toMap;
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

    private ExecutorService executorService;

    public RegionServiceImpl(AreaService areaService, CityService cityService, StateService stateService,
                             CountryService countryService, ExecutorService executorService) {
        this.areaService = areaService;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
        this.executorService = executorService;
    }

    private final Function<Long, AreaRegion> AREA_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            AreaInfo areaInfo = areaService.getAreaInfoById(id);
            return new AreaRegion(id, countryService.getCountryInfoById(areaInfo.getCountryId()), stateService.getStateInfoById(areaInfo.getStateId()),
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
                                            just(new AreaRegion(id, tuple3.getT1(), tuple3.getT2(), tuple3.getT3(), areaInfo))))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));


    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            CityInfo cityInfo = cityService.getCityInfoById(id);
            return new CityRegion(id, countryService.getCountryInfoById(cityInfo.getCountryId()), stateService.getStateInfoById(cityInfo.getStateId()), cityInfo);
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, AreaRegion>> AREA_REGIONS_GETTER = ids -> {
        if (isValidIdentities(ids)) {
            Collection<AreaInfo> areaInfos = areaService.selectAreaInfoByIds(ids).values();

            int size = areaInfos.size();
            List<Long> countryIds = new ArrayList<>(size);
            List<Long> stateIds = new ArrayList<>(size);
            List<Long> cityIds = new ArrayList<>(size);

            for (AreaInfo ai : areaInfos) {
                countryIds.add(ai.getCountryId());
                stateIds.add(ai.getStateId());
                cityIds.add(ai.getCityId());
            }

            CompletableFuture<Map<Long, CityInfo>> cityCf = supplyAsync(() -> cityService.selectCityInfoByIds(cityIds), executorService);
            CompletableFuture<Map<Long, StateInfo>> stateCf = supplyAsync(() -> stateService.selectStateInfoByIds(stateIds), executorService);
            CompletableFuture<Map<Long, CountryInfo>> countryCf = supplyAsync(() -> countryService.selectCountryInfoByIds(countryIds), executorService);

            allOf(cityCf, stateCf, countryCf).join();

            Map<Long, CountryInfo> countryInfoMap = countryCf.join();
            Map<Long, StateInfo> stateInfoMap = stateCf.join();
            Map<Long, CityInfo> cityInfoMap = cityCf.join();

            return areaInfos.parallelStream().map(ai -> new AreaRegion(ai.getId(), countryInfoMap.get(ai.getCountryId()),
                            stateInfoMap.get(ai.getStateId()), cityInfoMap.get(ai.getCityId()), ai))
                    .collect(toMap(AreaRegion::getAreaId, ar -> ar, (a, b) -> a));
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Mono<Map<Long, AreaRegion>>> AREA_REGIONS_MONO_GETTER = ids -> {
        if (isValidIdentities(ids)) {
            Collection<AreaInfo> areaInfos = areaService.selectAreaInfoByIds(ids).values();

            int size = areaInfos.size();
            List<Long> countryIds = new ArrayList<>(size);
            List<Long> stateIds = new ArrayList<>(size);
            List<Long> cityIds = new ArrayList<>(size);

            for (AreaInfo ai : areaInfos) {
                countryIds.add(ai.getCountryId());
                stateIds.add(ai.getStateId());
                cityIds.add(ai.getCityId());
            }

            return zip(
                    cityService.selectCityInfoMonoByIds(cityIds),
                    stateService.selectStateInfoMonoByIds(stateIds),
                    countryService.selectCountryInfoMonoByIds(countryIds)
            ).flatMap(tuple3 -> {
                Map<Long, CityInfo> cityInfoMap = tuple3.getT1();
                Map<Long, StateInfo> stateInfoMap = tuple3.getT2();
                Map<Long, CountryInfo> countryInfoMap = tuple3.getT3();

                return just(areaInfos.parallelStream().map(ai -> new AreaRegion(ai.getId(), countryInfoMap.get(ai.getCountryId()),
                                stateInfoMap.get(ai.getStateId()), cityInfoMap.get(ai.getCityId()), ai))
                        .collect(toMap(AreaRegion::getAreaId, ar -> ar, (a, b) -> a)));
            });
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
                                            just(new CityRegion(id, tuple2.getT1(), tuple2.getT2(), cityInfo))))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));


    private final Function<Long, StateRegion> STATE_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            StateInfo stateInfo = stateService.getStateInfoById(id);
            return new StateRegion(id, countryService.getCountryInfoById(stateInfo.getCountryId()), stateInfo);
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, Mono<StateRegion>> STATE_REGION_MONO_GETTER = id ->
            isValidIdentity(id) ?
                    stateService.getStateInfoMonoById(id)
                            .flatMap(stateInfo ->
                                    countryService.getCountryInfoMonoById(stateInfo.getCountryId())
                                            .flatMap(countryInfo -> just(new StateRegion(id, countryInfo, stateInfo))))
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
        return AREA_REGIONS_GETTER.apply(ids);
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
        return AREA_REGIONS_MONO_GETTER.apply(ids);
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
