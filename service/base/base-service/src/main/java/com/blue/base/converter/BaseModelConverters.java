package com.blue.base.converter;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.entity.Country;
import com.blue.base.repository.entity.State;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in base project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class BaseModelConverters {

    /**
     * country -> country info
     */
    public static final Function<Country, CountryInfo> COUNTRY_2_COUNTRY_INFO_CONVERTER = country -> {
        if (country == null)
            throw new BlueException(EMPTY_PARAM);

        return new CountryInfo(country.getId(), country.getName(), country.getCountryCode(), country.getPhoneCode(), country.getEmoji(), country.getEmojiu());
    };

    /**
     * countries -> country infos
     */
    public static final Function<List<Country>, List<CountryInfo>> COUNTRIES_2_COUNTRY_INFOS_CONVERTER = cs ->
            cs != null && cs.size() > 0 ? cs.stream()
                    .map(COUNTRY_2_COUNTRY_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * state -> state info
     */
    public static final Function<State, StateInfo> STATE_2_STATE_INFO_CONVERTER = state -> {
        if (state == null)
            throw new BlueException(EMPTY_PARAM);

        return new StateInfo(state.getId(), state.getName(), state.getStateCode());
    };

    /**
     * states -> state infos
     */
    public static final Function<List<State>, List<StateInfo>> STATES_2_STATE_INFOS_CONVERTER = ss ->
            ss != null && ss.size() > 0 ? ss.stream()
                    .map(STATE_2_STATE_INFO_CONVERTER)
                    .collect(toList()) : emptyList();


    /**
     * city -> city info
     */
    public static final Function<City, CityInfo> CITY_2_CITY_INFO_CONVERTER = city -> {
        if (city == null)
            throw new BlueException(EMPTY_PARAM);

        return new CityInfo(city.getId(), city.getName());
    };

    /**
     * cities -> city infos
     */
    public static final Function<List<City>, List<CityInfo>> CITIES_2_CITY_INFOS_CONVERTER = cs ->
            cs != null && cs.size() > 0 ? cs.stream()
                    .map(CITY_2_CITY_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

}
