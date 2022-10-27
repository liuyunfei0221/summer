package com.blue.base.converter;

import com.blue.base.api.model.*;
import com.blue.base.model.StyleInsertParam;
import com.blue.basic.model.exps.BlueException;
import com.blue.base.repository.entity.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueBoolean.FALSE;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Status.VALID;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * model converters in base project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class BaseModelConverters {

    /**
     * country -> country info
     */
    public static final Function<Country, CountryInfo> COUNTRY_2_COUNTRY_INFO_CONVERTER = country -> {
        if (isNull(country))
            throw new BlueException(EMPTY_PARAM);

        return new CountryInfo(country.getId(), country.getName(), country.getNativeName(), country.getNumericCode(), country.getCountryCode(),
                country.getPhoneCode(), country.getCapital(), country.getCurrency(), country.getCurrencySymbol(), country.getTopLevelDomain(),
                country.getRegion(), country.getEmoji(), country.getEmojiu());
    };

    /**
     * countries -> country info
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

        return new StateInfo(state.getId(), state.getCountryId(), state.getName(), state.getFipsCode(), state.getStateCode());
    };

    /**
     * states -> state info
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
     * cities -> city info
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
     * areas -> area info
     */
    public static final Function<List<Area>, List<AreaInfo>> AREAS_2_AREA_INFOS_CONVERTER = as ->
            isNotEmpty(as) ? as.stream()
                    .map(AREA_2_AREA_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * style -> styleInfo
     */
    public static final Function<Style, StyleInfo> STYLE_2_STYLE_INFO_CONVERTER = style -> {
        if (isNull(style))
            throw new BlueException(EMPTY_PARAM);

        return new StyleInfo(style.getId(), style.getName(), style.getAttributes(), style.getType(), style.getStatus());
    };

    /**
     * style insert param -> style
     */
    public static final Function<StyleInsertParam, Style> STYLE_INSERT_PARAM_2_STYLE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Style style = new Style();

        style.setName(param.getName());
        style.setAttributes(param.getAttributes());
        style.setType(param.getType());
        style.setIsActive(FALSE.bool);
        style.setStatus(VALID.status);
        style.setCreateTime(stamp);
        style.setUpdateTime(stamp);

        return style;
    };

    public static final BiFunction<Style, Map<Long, String>, StyleManagerInfo> STYLE_2_STYLE_MANAGER_INFO_CONVERTER = (style, idAndMemberNameMapping) -> {
        if (isNull(style))
            throw new BlueException(EMPTY_PARAM);

        return new StyleManagerInfo(style.getId(), style.getName(), style.getAttributes(), style.getType(), style.getIsActive(), style.getStatus(),
                style.getCreateTime(), style.getUpdateTime(), style.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(style.getCreator())).orElse(EMPTY_VALUE.value),
                style.getUpdater(), ofNullable(idAndMemberNameMapping).map(m -> m.get(style.getUpdater())).orElse(EMPTY_VALUE.value));
    };

    /**
     * type -> type info
     */
    public static final Function<DictType, DictTypeInfo> DICT_TYPE_2_DICT_TYPE_INFO_CONVERTER = type -> {
        if (isNull(type))
            throw new BlueException(EMPTY_PARAM);

        return new DictTypeInfo(type.getId(), type.getCode(), type.getName());
    };

    /**
     * types -> type info
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
     * dict -> dict info
     */
    public static final Function<List<Dict>, List<DictInfo>> DICT_2_DICT_INFOS_CONVERTER = ds ->
            isNotEmpty(ds) ? ds.stream()
                    .map(DICT_2_DICT_INFO_CONVERTER)
                    .collect(toList()) : emptyList();


}
