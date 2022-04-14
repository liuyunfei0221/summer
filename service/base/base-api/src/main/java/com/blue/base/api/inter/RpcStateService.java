package com.blue.base.api.inter;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * rpc state interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcStateService {

    /**
     * get state info by id
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
    CompletableFuture<StateInfo> getStateInfoMonoById(Long id);

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
    CompletableFuture<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId);

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids);

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids);

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
    CompletableFuture<StateRegion> getStateRegionMonoById(Long id);

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
    CompletableFuture<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids);

}
