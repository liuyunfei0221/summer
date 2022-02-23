package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.mapper.StateMapper;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.BlueCheck.isInvalidIdentity;
import static com.blue.base.common.base.BlueCheck.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.converter.BaseModelConverters.STATES_2_STATE_INFOS_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * state service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class StateServiceImpl implements StateService {

    private static final Logger LOGGER = getLogger(StateServiceImpl.class);

    private final StateMapper stateMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StateServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, StateMapper stateMapper) {
        this.stateMapper = stateMapper;

        COUNTRY_ID_STATE_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<StateInfo>> COUNTRY_ID_STATE_CACHE;

    private final Function<Long, List<StateInfo>> DB_STATE_GETTER = cid -> {
        List<State> states = this.selectStateByCountryId(cid);

        LOGGER.info("DB_STATE_GETTER, cid = {}, states = {}", cid, states);
        return STATES_2_STATE_INFOS_CONVERTER.apply(
                states.stream().sorted(Comparator.comparing(State::getStateCode)).collect(toList()));
    };

    /**
     * get state by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<State> getStateById(Long id) {
        LOGGER.info("Optional<State> getStateById(Long id), id = {}", id);

        return ofNullable(stateMapper.selectByPrimaryKey(id));
    }

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<State> selectStateByCountryId(Long countryId) {
        LOGGER.info("List<State> selectStateByCountryId(Long countryId), countryId = {}", countryId);

        if (isInvalidIdentity(countryId))
            throw new BlueException(INVALID_IDENTITY);

        return stateMapper.selectByCountryId(countryId);
    }

    /**
     * select all states
     *
     * @return
     */
    @Override
    public List<State> selectState() {
        LOGGER.info("List<State> selectState()");

        return stateMapper.select();
    }

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId) {
        LOGGER.info("Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId), countryId = {}", countryId);

        return isValidIdentity(countryId)
                ?
                justOrEmpty(COUNTRY_ID_STATE_CACHE.get(countryId, DB_STATE_GETTER)).switchIfEmpty(just(emptyList()))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid state infos
     *
     * @return
     */
    @Override
    public void invalidStateInfosCache() {
        LOGGER.info("void invalidStateInfosCache()");

        COUNTRY_ID_STATE_CACHE.invalidateAll();
    }

}
