package com.blue.secure.converter;

import com.blue.base.common.base.CommonFunctions;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.common.base.ConstantProcessor.getResourceTypeByIdentity;
import static com.blue.base.constant.base.CommonException.EMPTY_PARAM_EXP;
import static com.blue.base.constant.base.Default.NOT_DEFAULT;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;
import static com.blue.secure.constant.SecureCommonException.BLANK_DESC_EXP;
import static com.blue.secure.constant.SecureCommonException.BLANK_NAME_EXP;

/**
 * model converters in secure project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class SecureModelConverters {

    private static final Supplier<Long> TIME_STAMP_GETTER = CommonFunctions.TIME_STAMP_GETTER;

    /**
     * role insert param -> role
     */
    public static final Function<RoleInsertParam, Role> ROLE_INSERT_PARAM_2_ROLE_CONVERTER = param -> {
        if (param == null)
            throw EMPTY_PARAM_EXP.exp;

        Role role = new Role();

        String name = param.getName();
        if (isBlank(name))
            throw BLANK_NAME_EXP.exp;

        String description = param.getDescription();
        if (isBlank(description))
            throw BLANK_DESC_EXP.exp;

        role.setName(name);
        role.setDescription(description);
        role.setIsDefault(NOT_DEFAULT.status);

        Long stamp = TIME_STAMP_GETTER.get();
        role.setCreateTime(stamp);
        role.setUpdateTime(stamp);

        return role;
    };

    /**
     * role -> role info
     */
    public static final Function<Role, RoleInfo> ROLE_2_ROLE_INFO_CONVERTER = role -> {
        if (role == null)
            throw EMPTY_PARAM_EXP.exp;

        return new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getIsDefault());
    };

    /**
     * resource -> resource info
     */
    public static final Function<Resource, ResourceInfo> RESOURCE_2_RESOURCE_INFO_CONVERTER = resource -> {
        if (resource == null)
            throw EMPTY_PARAM_EXP.exp;

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(),
                resource.getAuthenticate(), resource.getRequestUnDecryption(), resource.getResponseUnEncryption(),
                resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription());
    };

}
