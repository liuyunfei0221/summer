package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new role
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RoleInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -4099297009366670202L;

    private String name;

    private String description;

    private Integer level;

    public RoleInsertParam() {
    }

    public RoleInsertParam(String name, String description, Integer level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");
        if (isNull(this.level) || this.level < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "level can't be null or less than 1");
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "RoleInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                '}';
    }

}
