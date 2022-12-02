package com.blue.risk.converter;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.model.IllegalMarkParam;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyManagerInfo;
import com.blue.risk.repository.entity.RiskStrategy;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_JSON;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;

/**
 * model converters in risk project
 *
 * @author liuyunfei
 */
@SuppressWarnings("unchecked")
public final class RiskModelConverters {

    public static final Function<String, Map<String, String>> ATTR_JSON_2_ATTR_MAP_CONVERTER = attrJson ->
            ofNullable(attrJson).filter(BlueChecker::isNotBlank)
                    .map(attr -> (Map<String, String>) GSON.fromJson(attr, STRING_MAP_TYPE))
                    .orElseGet(Collections::emptyMap);

    public static final Function<Map<String, String>, String> ATTR_MAP_2_ATTR_JSON_CONVERTER = attrMap ->
            ofNullable(attrMap).filter(BlueChecker::isNotEmpty)
                    .map(GSON::toJson).orElse(EMPTY_JSON.value);

    /**
     * risk strategy -> risk strategy info
     */
    public static final Function<RiskStrategy, RiskStrategyInfo> RISK_STRATEGY_2_RISK_STRATEGY_INFO_CONVERTER = strategy -> {
        if (isNull(strategy))
            throw new BlueException(EMPTY_PARAM);

        return new RiskStrategyInfo(strategy.getId(), strategy.getType(), ATTR_JSON_2_ATTR_MAP_CONVERTER.apply(strategy.getAttributes()), strategy.getEnable());
    };

    /**
     * risk strategy insert param -> risk strategy
     */
    public static final Function<RiskStrategyInsertParam, RiskStrategy> RISK_STRATEGY_INSERT_PARAM_2_RISK_STRATEGY_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        RiskStrategy riskStrategy = new RiskStrategy();

        riskStrategy.setName(param.getName());
        riskStrategy.setDescription(param.getDescription());
        riskStrategy.setType(param.getType());

        String attrJson;
        try {
            attrJson = ATTR_MAP_2_ATTR_JSON_CONVERTER.apply(param.getAttributes());
        } catch (Exception e) {
            throw new BlueException(INVALID_PARAM);
        }

        riskStrategy.setAttributes(attrJson);
        riskStrategy.setEnable(param.getEnable());

        Long stamp = TIME_STAMP_GETTER.get();

        riskStrategy.setCreateTime(stamp);
        riskStrategy.setUpdateTime(stamp);

        return riskStrategy;
    };

    public static final BiFunction<RiskStrategy, Map<Long, String>, RiskStrategyManagerInfo> RISK_STRATEGY_2_RISK_STRATEGY_MANAGER_INFOS_CONVERTER = (riskStrategy, idAndMemberNameMapping) -> {
        if (isNull(riskStrategy))
            throw new BlueException(EMPTY_PARAM);

        return new RiskStrategyManagerInfo(riskStrategy.getId(), riskStrategy.getName(), riskStrategy.getDescription(), riskStrategy.getType(), ATTR_JSON_2_ATTR_MAP_CONVERTER.apply(riskStrategy.getAttributes()),
                riskStrategy.getEnable(), riskStrategy.getCreateTime(), riskStrategy.getUpdateTime(), riskStrategy.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(riskStrategy.getCreator())).orElse(EMPTY_VALUE.value),
                riskStrategy.getUpdater(), ofNullable(idAndMemberNameMapping).map(m -> m.get(riskStrategy.getUpdater())).orElse(EMPTY_VALUE.value));
    };


    public static final Function<IllegalMarkParam, IllegalMarkEvent> ILLEGAL_MARK_PARAM_2_ILLEGAL_MARK_EVENT_CONVERTER = illegalMarkParam -> {
        if (isNull(illegalMarkParam))
            throw new BlueException(EMPTY_PARAM);
        illegalMarkParam.asserts();

        return new IllegalMarkEvent(String.valueOf(illegalMarkParam.getMemberId()), illegalMarkParam.getIp(), illegalMarkParam.getMethod(),
                illegalMarkParam.getUri(), illegalMarkParam.getMark(), illegalMarkParam.getIllegalExpiresSecond());
    };

}
