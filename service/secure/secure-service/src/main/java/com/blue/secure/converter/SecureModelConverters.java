package com.blue.secure.converter;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.common.base.ConstantProcessor.getResourceTypeByIdentity;
import static com.blue.base.constant.base.Default.NOT_DEFAULT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;

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
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "param can't be null");

        Role role = new Role();

        String name = param.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = param.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

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
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "role can't be null");

        return new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getIsDefault());
    };

    /**
     * resource -> resource info
     */
    public static final Function<Resource, ResourceInfo> RESOURCE_2_RESOURCE_INFO_CONVERTER = resource -> {
        if (resource == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "resource can't be null");

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(),
                resource.getAuthenticate(), resource.getRequestUnDecryption(), resource.getResponseUnEncryption(),
                resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription());
    };

}
