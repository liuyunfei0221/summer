package com.blue.base.service.impl;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.base.event.producer.RegionInfosInvalidProducer;
import com.blue.base.model.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.base.service.inter.*;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.SummerAttr.EMPTY_EVENT;
import static com.blue.basic.constant.common.SyncKey.REGION_UPDATE_SYNC;
import static reactor.core.publisher.Mono.fromRunnable;

/**
 * config country,state,city,area impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class RegionControlServiceImpl implements RegionControlService {

    private static final Logger LOGGER = Loggers.getLogger(RegionControlServiceImpl.class);

    private final CountryService countryService;

    private final StateService stateService;

    private final CityService cityService;

    private final AreaService areaService;

    private final RegionInfosInvalidProducer regionInfosInvalidProducer;

    private final SynchronizedProcessor synchronizedProcessor;

    public RegionControlServiceImpl(CountryService countryService, StateService stateService, CityService cityService,
                                    AreaService areaService, RegionInfosInvalidProducer regionInfosInvalidProducer,
                                    SynchronizedProcessor synchronizedProcessor) {
        this.countryService = countryService;
        this.stateService = stateService;
        this.cityService = cityService;
        this.areaService = areaService;
        this.regionInfosInvalidProducer = regionInfosInvalidProducer;
        this.synchronizedProcessor = synchronizedProcessor;
    }

    /**
     * insert country
     *
     * @param countryInsertParam
     * @return
     */
    @Override
    public Mono<CountryInfo> insertCountry(CountryInsertParam countryInsertParam) {
        LOGGER.info("Mono<CountryInfo> insertCountry(CountryInsertParam countryInsertParam), countryInsertParam = {}", countryInsertParam);
        countryInsertParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                countryService.insertCountry(countryInsertParam)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update country
     *
     * @param countryUpdateParam
     * @return
     */
    @Override
    public Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam) {
        LOGGER.info("Mono<CountryInfo> updateCountry(CountryUpdateParam countryUpdateParam), countryUpdateParam = {}", countryUpdateParam);
        countryUpdateParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                countryService.updateCountry(countryUpdateParam)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete country
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CountryInfo> deleteCountry(Long id) {
        LOGGER.info("Mono<CountryInfo> deleteCountry(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                countryService.deleteCountry(id)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * insert state
     *
     * @param stateInsertParam
     * @return
     */
    @Override
    public Mono<StateInfo> insertState(StateInsertParam stateInsertParam) {
        LOGGER.info("Mono<StateInfo> insertState(StateInsertParam stateInsertParam), stateInsertParam = {}", stateInsertParam);
        stateInsertParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                stateService.insertState(stateInsertParam)
        ).doOnSuccess(si -> {
            LOGGER.info("si = {}", si);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update state
     *
     * @param stateUpdateParam
     * @return
     */
    @Override
    public Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam) {
        LOGGER.info("Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam), stateUpdateParam = {}", stateUpdateParam);
        stateUpdateParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                stateService.updateState(stateUpdateParam)
        ).doOnSuccess(si -> {
            LOGGER.info("si = {}", si);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete state
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateInfo> deleteState(Long id) {
        LOGGER.info("Mono<StateInfo> deleteState(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                stateService.deleteState(id)
        ).doOnSuccess(si -> {
            LOGGER.info("si = {}", si);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * insert city
     *
     * @param cityInsertParam
     * @return
     */
    @Override
    public Mono<CityInfo> insertCity(CityInsertParam cityInsertParam) {
        LOGGER.info("Mono<CityInfo> insertCity(CityInsertParam cityInsertParam), cityInsertParam = {}", cityInsertParam);
        cityInsertParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                cityService.insertCity(cityInsertParam)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update city
     *
     * @param cityUpdateParam
     * @return
     */
    @Override
    public Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam) {
        LOGGER.info("Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam), cityUpdateParam = {}", cityUpdateParam);
        cityUpdateParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                cityService.updateCity(cityUpdateParam)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete city
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> deleteCity(Long id) {
        LOGGER.info("Mono<CityInfo> deleteCity(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                cityService.deleteCity(id)
        ).doOnSuccess(ci -> {
            LOGGER.info("ci = {}", ci);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * insert area
     *
     * @param areaInsertParam
     * @return
     */
    @Override
    public Mono<AreaInfo> insertArea(AreaInsertParam areaInsertParam) {
        LOGGER.info("Mono<AreaInfo> insertArea(AreaInsertParam areaInsertParam), areaInsertParam = {}", areaInsertParam);
        areaInsertParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                areaService.insertArea(areaInsertParam)
        ).doOnSuccess(ai -> {
            LOGGER.info("ai = {}", ai);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update area
     *
     * @param areaUpdateParam
     * @return
     */
    @Override
    public Mono<AreaInfo> updateArea(AreaUpdateParam areaUpdateParam) {
        LOGGER.info("Mono<AreaInfo> updateArea(AreaUpdateParam areaUpdateParam), areaUpdateParam = {}", areaUpdateParam);
        areaUpdateParam.asserts();

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                areaService.updateArea(areaUpdateParam)
        ).doOnSuccess(ai -> {
            LOGGER.info("ai = {}", ai);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete area
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> deleteArea(Long id) {
        LOGGER.info("Mono<AreaInfo> deleteArea(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(REGION_UPDATE_SYNC.key, () ->
                areaService.deleteArea(id)
        ).doOnSuccess(ai -> {
            LOGGER.info("ai = {}", ai);
            regionInfosInvalidProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * invalid all cache
     *
     * @return
     */
    @Override
    public Mono<Void> invalidAllCache() {
        return fromRunnable(() -> {
            countryService.invalidCache();
            stateService.invalidCache();
            cityService.invalidCache();
            areaService.invalidCache();
        }).then();
    }

}
