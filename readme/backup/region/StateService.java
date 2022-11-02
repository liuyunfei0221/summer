package com.blue.base.service.inter;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.model.StateCondition;
import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.repository.entity.State;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
     * invalid cache
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
    Mono<State> getStateById(Long id);

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    Mono<List<State>> selectStateByCountryId(Long countryId);

    /**
     * select states by ids
     *
     * @param ids
     * @return
     */
    Mono<List<State>> selectStateByIds(List<Long> ids);

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    Mono<StateInfo> getStateInfoById(Long id);

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId);

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, StateInfo>> selectStateInfoByIds(List<Long> ids);

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    Mono<StateRegion> getStateRegionById(Long id);

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, StateRegion>> selectStateRegionByIds(List<Long> ids);

    /**
     * select state by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<State>> selectStateByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count state by query
     *
     * @param query
     * @return
     */
    Mono<Long> countStateByQuery(Query query);

    /**
     * select state info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<StateInfo>> selectStatePageByPageAndCondition(PageModelRequest<StateCondition> pageModelRequest);

}
