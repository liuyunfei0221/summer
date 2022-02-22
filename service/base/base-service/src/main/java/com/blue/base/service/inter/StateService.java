package com.blue.base.service.inter;

import com.blue.base.api.model.StateInfo;
import com.blue.base.repository.entity.State;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * state service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface StateService {

    /**
     * get state by state id
     *
     * @param id
     * @return
     */
    Optional<State> getStateById(Long id);

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    List<State> selectStateByCountryId(Long countryId);

    /**
     * select all states
     *
     * @return
     */
    List<State> selectState();

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId);

    /**
     * expire state infos
     *
     * @return
     */
    void invalidStateInfosCache();

}
