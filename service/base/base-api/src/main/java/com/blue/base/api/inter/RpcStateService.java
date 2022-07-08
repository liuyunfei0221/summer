package com.blue.base.api.inter;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;

import java.util.List;
import java.util.Map;
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
    CompletableFuture<StateInfo> getStateInfoById(Long id);

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    CompletableFuture<List<StateInfo>> selectStateInfoByCountryId(Long countryId);

    /**
     * select state info by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, StateInfo>> selectStateInfoByIds(List<Long> ids);

    /**
     * get state region by id
     *
     * @param id
     * @return
     */
    CompletableFuture<StateRegion> getStateRegionById(Long id);

    /**
     * select state regions by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long, StateRegion>> selectStateRegionByIds(List<Long> ids);

}
