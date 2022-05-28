package com.blue.base.service.inter;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.model.StateCondition;
import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.repository.entity.State;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * state service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface StateService {

    /**
     * insert state
     *
     * @param stateInsertParam
     * @return
     */
    Mono<StateInfo> insertState(StateInsertParam stateInsertParam);

    /**
     * update state
     *
     * @param stateUpdateParam
     * @return
     */
    Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam);

    /**
     * delete state
     *
     * @param id
     * @return
     */
    Mono<StateInfo> deleteState(Long id);

    /**
     * invalid chche
     */
    void invalidCache();

    /**
     * a state's countryId was changed
     *
     * @param countryId
     * @param stateId
     * @return
     */
    Mono<Long> updateCountryIdOfCityByStateId(Long countryId, Long stateId);

    /**
     * a state's countryId was changed
     *
     * @param countryId
     * @param stateId
     * @return
     */
    Mono<Long> updateCountryIdOfAreaByStateId(Long countryId, Long stateId);

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
     * select state info by ids
     *
     * @param ids
     * @return
     */
    Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids);

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids);

    /**
     * get state region by id
     *
     * @param id
     * @return
     */
    StateRegion getStateRegionById(Long id);

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    Mono<StateRegion> getStateRegionMonoById(Long id);

    /**
     * select state regions by ids
     *
     * @param ids
     * @return
     */
    Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids);

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids);

    /**
     * invalid state info
     *
     * @return
     */
    void invalidStateInfosCache();

    /**
     * select state by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<State>> selectStateMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count state by query
     *
     * @param query
     * @return
     */
    Mono<Long> countStateMonoByQuery(Query query);

    /**
     * select state info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<StateInfo>> selectStatePageMonoByPageAndCondition(PageModelRequest<StateCondition> pageModelRequest);

}
