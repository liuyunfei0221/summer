package com.blue.base.api.inter;

import com.blue.base.api.model.CountryInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    Optional<CountryInfo> getCountryInfoOptById(Long id);

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    CountryInfo getCountryInfoById(Long id);

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    CompletableFuture<CountryInfo> getCountryInfoMonoById(Long id);

    /**
     * select all countries
     *
     * @return
     */
    List<CountryInfo> selectCountryInfo();

    /**
     * select all countries mono
     *
     * @return
     */
    CompletableFuture<List<CountryInfo>> selectCountryInfoMono();

    /**
     * select country infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids);

    /**
     * select country infos mono by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<Map<Long,CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids);

}
