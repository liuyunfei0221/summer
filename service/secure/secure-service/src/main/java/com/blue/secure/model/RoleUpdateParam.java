package com.blue.secure.model;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.*;

/**
 * params for update a exist role
 *
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RoleUpdateParam extends RoleInsertParam {

    private static final long serialVersionUID = 3709726547884800171L;

    private Long id;

    public RoleUpdateParam() {
    }

    public RoleUpdateParam(Long id, String name, String description, Integer level) {
        super(name, description, level);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        this.id = id;
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
        return "RoleUpdateParam{" +
                "id=" + id +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                '}';
    }
}
