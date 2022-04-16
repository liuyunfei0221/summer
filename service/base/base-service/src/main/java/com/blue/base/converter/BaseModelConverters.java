package com.blue.base.converter;

import com.blue.base.api.model.*;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.*;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNotEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in base project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class BaseModelConverters {

    /**
     * country -> country info
     */
    public static final Function<Country, CountryInfo> COUNTRY_2_COUNTRY_INFO_CONVERTER = country -> {
        if (isNull(country))
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
        if (isNull(state))
            throw new BlueException(EMPTY_PARAM);

        return new StateInfo(state.getId(), state.getCountryId(), state.getName(), state.getFipsCode(), state.getCountryCode(), state.getStateCode());
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
        if (isNull(city))
            throw new BlueException(EMPTY_PARAM);

        return new CityInfo(city.getId(), city.getCountryId(), city.getStateId(), city.getName());
    };

    /**
     * cities -> city infos
     */
    public static final Function<List<City>, List<CityInfo>> CITIES_2_CITY_INFOS_CONVERTER = cs ->
            cs != null && cs.size() > 0 ? cs.stream()
                    .map(CITY_2_CITY_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * area -> area info
     */
    public static final Function<Area, AreaInfo> AREA_2_AREA_INFO_CONVERTER = area -> {
        if (isNull(area))
            throw new BlueException(EMPTY_PARAM);

        return new AreaInfo(area.getId(), area.getCountryId(), area.getStateId(), area.getCityId(), area.getName());
    };

    /**
     * areas -> area infos
     */
    public static final Function<List<Area>, List<AreaInfo>> AREAS_2_AREA_INFOS_CONVERTER = as ->
            isNotEmpty(as) ? as.stream()
                    .map(AREA_2_AREA_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * type -> type info
     */
    public static final Function<DictType, DictTypeInfo> DICT_TYPE_2_DICT_TYPE_INFO_CONVERTER = type -> {
        if (isNull(type))
            throw new BlueException(EMPTY_PARAM);

        return new DictTypeInfo(type.getId(), type.getCode(), type.getName());
    };

    /**
     * types -> type infos
     */
    public static final Function<List<DictType>, List<DictTypeInfo>> DICT_TYPES_2_DICT_TYPE_INFOS_CONVERTER = ts ->
            isNotEmpty(ts) ? ts.stream()
                    .map(DICT_TYPE_2_DICT_TYPE_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * dict -> dict info
     */
    public static final Function<Dict, DictInfo> DICT_2_DICT_INFO_CONVERTER = dict -> {
        if (isNull(dict))
            throw new BlueException(EMPTY_PARAM);

        return new DictInfo(dict.getId(), dict.getName(), dict.getValue());
    };

    /**
     * dict -> dict infos
     */
    public static final Function<List<Dict>, List<DictInfo>> DICT_2_DICT_INFOS_CONVERTER = ds ->
            isNotEmpty(ds) ? ds.stream()
                    .map(DICT_2_DICT_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

}
