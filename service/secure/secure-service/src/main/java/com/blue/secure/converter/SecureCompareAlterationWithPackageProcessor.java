package com.blue.secure.converter;

import com.blue.secure.model.RoleUpdateParam;
import com.blue.secure.repository.entity.Role;

import java.util.function.BiFunction;

import static com.blue.base.common.base.Asserter.isNotBlank;
import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class SecureCompareAlterationWithPackageProcessor {

    /**
     * for role
     */
    public static final BiFunction<RoleUpdateParam, Role, Boolean> ROLE_UPDATE_PARAM_AND_ROLE_COMPARER = (p, t) -> {
        if (!p.getId().equals(t.getId()))
            throw BAD_REQUEST_EXP.exp;

        boolean alteration = false;

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        String description = p.getDescription();
        if (isNotBlank(description) && !description.equals(t.getDescription())) {
            t.setDescription(description);
            alteration = true;
        }

        return alteration;
    };

}
