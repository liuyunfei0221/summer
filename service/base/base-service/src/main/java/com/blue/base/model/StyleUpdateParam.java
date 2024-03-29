package com.blue.base.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist style
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class StyleUpdateParam extends StyleInsertParam {

    private static final long serialVersionUID = 4092725047795505179L;

    private Long id;

    public StyleUpdateParam() {
    }

    public StyleUpdateParam(Long id, String name, String attributes, Integer type) {
        super(name, attributes, type);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
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
        return "StyleUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attributes='" + attributes + '\'' +
                ", type=" + type +
                '}';
    }

}
