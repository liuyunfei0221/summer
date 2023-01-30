package com.blue.risk.model;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertRiskType;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * params for create a new risk strategy
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RiskStrategyInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4411747600578530850L;

    protected String name;

    protected String description;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    protected Integer type;

    /**
     * attributes json
     */
    protected Map<String, String> attributes;

    protected Boolean enable;

    public RiskStrategyInsertParam() {
    }

    public RiskStrategyInsertParam(String name, String description, Integer type, Map<String, String> attributes, Boolean enable) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.attributes = attributes;
        this.enable = enable;
    }

    @Override
    public void asserts() {
        int len;
        if (isBlank(this.name) || (len = this.name.length()) < (int) NAME_LEN_MIN.value || len > (int) NAME_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.description) || (len = this.description.length()) < (int) DESCRIPTION_LEN_MIN.value || len > (int) DESCRIPTION_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid link");
        assertRiskType(this.type, false);

        if (isEmpty(attributes))
            throw new BlueException(INVALID_PARAM);
        if (attributes.keySet().stream().anyMatch(BlueChecker::isBlank))
            throw new BlueException(INVALID_PARAM);
        if (attributes.values().stream().anyMatch(BlueChecker::isBlank))
            throw new BlueException(INVALID_PARAM);

        if (isNull(enable))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid enable");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "RiskStrategyInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", attributes='" + attributes + '\'' +
                ", enable=" + enable +
                '}';
    }

}