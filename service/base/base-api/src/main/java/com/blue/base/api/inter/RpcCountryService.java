package com.blue.base.api.inter;

import com.blue.base.api.model.CountryInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * rpc country interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcCountryService {

    /**
     * get country info by country id
     *
     * @param id
     * @return
     */
    CompletableFuture<CountryInfo> getCountryInfoById(Long id);

    /**
     * select all countries
     *
     * @return
     */
    CompletableFuture<List<CountryInfo>> selectCountryInfo();

    /**
     * select country info by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long,CountryInfo>> selectCountryInfoByIds(List<Long> ids);

}
