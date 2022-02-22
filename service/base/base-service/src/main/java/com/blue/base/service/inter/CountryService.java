package com.blue.base.service.inter;

import com.blue.base.api.model.CountryInfo;
import com.blue.base.repository.entity.Country;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * country service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
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
     * select all countries
     *
     * @return
     */
    Mono<List<CountryInfo>> selectCountryInfo();

    /**
     * expire country infos
     *
     * @return
     */
    void invalidCountryInfosCache();

}
