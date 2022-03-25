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
@SuppressWarnings({"JavaDoc", "unused"})
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
     * select states by ids
     *
     * @param ids
     * @return
     */
    List<State> selectStateByIds(List<Long> ids);

    /**
     * get state opt info by country id
     *
     * @param id
     * @return
     */
    Optional<StateInfo> getStateInfoOptById(Long id);

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    StateInfo getStateInfoById(Long id);

    /**
     * get state info mono by id
     *
     * @param id
     * @return
     */
    Mono<StateInfo> getStateInfoMonoById(Long id);

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    List<StateInfo> selectStateInfoByCountryId(Long countryId);

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    Mono<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId);

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    List<StateInfo> selectStateInfoByIds(List<Long> ids);

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<StateInfo>> selectStateInfoMonoByIds(List<Long> ids);

    /**
     * invalid state infos
     *
     * @return
     */
    void invalidStateInfosCache();

}
