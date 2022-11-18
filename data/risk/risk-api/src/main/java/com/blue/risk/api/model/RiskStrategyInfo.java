package com.blue.risk.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;
import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertRiskType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * risk strategy
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskStrategyInfo implements Serializable, Asserter {

    private static final long serialVersionUID = 3330908990559810748L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer type;

    /**
     * risk attributes
     */
    private Map<String, String> attributes;

    private Boolean enable;

    public RiskStrategyInfo() {
    }

    public RiskStrategyInfo(Long id, Integer type, Map<String, String> attributes, Boolean enable) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
        this.enable = enable;
    }

    @Override
    public void asserts() {
        assertRiskType(this.type, false);

        if (this.enable && isNull(attributes))
            throw new BlueException(INVALID_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "RiskStrategyInfo{" +
                "id=" + id +
                ", type=" + type +
                ", attributes=" + attributes +
                ", enable=" + enable +
                '}';
    }

}
