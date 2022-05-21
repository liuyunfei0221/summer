package com.blue.base.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new area
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AreaInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -3747454352086271819L;
    
    private Long cityId;

    private String name;

    public AreaInsertParam() {
    }

    public AreaInsertParam(Long cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.cityId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "cityId can't be null or less than 1");
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AreaInsertParam{" +
                "cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }

}
