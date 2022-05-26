package com.blue.base.service.inter;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.base.repository.entity.Country;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * country service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface CountryService {

    /**
     * insert country
     *
     * @param countryInsertParam
     * @return
     */
    Mono<StateInfo> insertCountry(CountryInsertParam countryInsertParam);

    /**
     * update country
     *
     * @param countryUpdateParam
     * @return
     */
    Mono<StateInfo> updateCountry(CountryUpdateParam countryUpdateParam);

    /**
     * delete country
     *
     * @param id
     * @return
     */
    Mono<StateInfo> deleteCountry(Long id);

    /**
     * get country by country id
     *
     * @param id
     * @return
     */
    Optional<Country> getCountryById(Long id);

    /**
     * select all countries
     *
     * @return
     */
    List<Country> selectCountry();

    /**
     * select countries by ids
     *
     * @return
     */
    List<Country> selectCountryByIds(List<Long> ids);

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
    Mono<CountryInfo> getCountryInfoMonoById(Long id);

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
    Mono<List<CountryInfo>> selectCountryInfoMono();

    /**
     * select country info by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids);

    /**
     * select country info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids);

    /**
     * invalid country info
     *
     * @return
     */
    void invalidCountryInfosCache();

}
