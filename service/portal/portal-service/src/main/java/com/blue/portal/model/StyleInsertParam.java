package com.blue.portal.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new style
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class StyleInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -1360908773352526780L;

    private String name;

    private String attributes;

    private Integer type;

    public StyleInsertParam() {
    }

    public StyleInsertParam(String name, String attributes, Integer type) {
        this.name = name;
        this.attributes = attributes;
        this.type = type;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.attributes))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "attributes can't be blank");
        assertBulletinType(this.type, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StyleInsertParam{" +
                "name='" + name + '\'' +
                ", attributes='" + attributes + '\'' +
                ", type=" + type +
                '}';
    }

}
