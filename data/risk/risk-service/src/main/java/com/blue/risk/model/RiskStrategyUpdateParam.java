package com.blue.risk.model;

import com.blue.basic.model.exps.BlueException;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.ConstantProcessor.assertRiskType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a risk strategy
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RiskStrategyUpdateParam extends RiskStrategyInsertParam {

    private static final long serialVersionUID = 8286001410300699530L;

    private Long id;

    public RiskStrategyUpdateParam() {
    }

    public RiskStrategyUpdateParam(Long id, String name, String description, Integer type, Map<String, String> attributes, Boolean enable) {
        super(name, description, type, attributes, enable);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        assertRiskType(this.type, false);
    }

    public Long getId() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RiskStrategyUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", attributes='" + attributes + '\'' +
                ", enable=" + enable +
                '}';
    }

}
