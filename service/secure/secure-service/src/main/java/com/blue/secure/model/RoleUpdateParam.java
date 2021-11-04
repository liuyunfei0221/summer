package com.blue.secure.model;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.constant.base.CommonException.INVALID_IDENTITY_EXP;

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

    public RoleUpdateParam(String name, String description, Long id) {
        super(name, description);
        if (isInvalidIdentity(id))
            throw INVALID_IDENTITY_EXP.exp;

        this.id = id;
    }

    public Long getId() {
        if (isInvalidIdentity(id))
            throw INVALID_IDENTITY_EXP.exp;

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
