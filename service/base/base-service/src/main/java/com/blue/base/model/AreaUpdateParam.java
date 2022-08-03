package com.blue.base.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;

/**
 * params for update exist area
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AreaUpdateParam extends AreaInsertParam {

    private static final long serialVersionUID = 6314794825778541938L;

    private Long id;

    public AreaUpdateParam() {
    }

    public AreaUpdateParam(Long id, Long cityId, String name) {
        super(cityId, name);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        if (isNull(super.getCityId()) && isBlank(super.getName()))
            throw new BlueException(EMPTY_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AreaUpdateParam{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }

}
