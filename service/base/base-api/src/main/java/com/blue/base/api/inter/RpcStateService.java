package com.blue.base.api.inter;

import com.blue.base.api.model.StateInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * rpc state interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcStateService {

    /**
     * get state info by country id
     *
     * @param id
     * @return
     */
    Optional<StateInfo> getStateInfoOptById(Long id);

    /**
     * get state info by country id with assert
     *
     * @param id
     * @return
     */
    StateInfo getStateInfoById(Long id);

    /**
     * get state info mono by country id
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

}
