package com.blue.base.service.inter;

import com.blue.base.api.model.CountryInfo;
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
     * select country infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long,CountryInfo> selectCountryInfoByIds(List<Long> ids);

    /**
     * select country infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long,CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids);

    /**
     * invalid country infos
     *
     * @return
     */
    void invalidCountryInfosCache();

}
