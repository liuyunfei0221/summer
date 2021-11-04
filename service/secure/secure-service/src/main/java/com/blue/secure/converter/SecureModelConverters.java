package com.blue.secure.converter;

import com.blue.base.common.base.CommonFunctions;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.common.base.ConstantProcessor.*;
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

        String name = param.getName();
        if (isBlank(name))
            throw BLANK_NAME_EXP.exp;

        String description = param.getDescription();
        if (isBlank(description))
            throw BLANK_DESC_EXP.exp;

        Long stamp = TIME_STAMP_GETTER.get();

        Role role = new Role();

        role.setName(name);
        role.setDescription(description);
        role.setIsDefault(NOT_DEFAULT.status);
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
     * resource insert param -> resource
     */
    public static final Function<ResourceInsertParam, Resource> RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER = param -> {
        if (param == null)
            throw EMPTY_PARAM_EXP.exp;

        String requestMethod = param.getRequestMethod();
        assertHttpMethod(requestMethod, false);

        String module = param.getModule();
        if (isBlank(module))
            throw BLANK_NAME_EXP.exp;

        String uri = param.getUri();
        if (isBlank(uri))
            throw BLANK_NAME_EXP.exp;

        Boolean authenticate = param.getAuthenticate();
        if (authenticate == null)
            throw BLANK_NAME_EXP.exp;

        Boolean requestUnDecryption = param.getRequestUnDecryption();
        if (requestUnDecryption == null)
            throw BLANK_NAME_EXP.exp;

        Boolean responseUnEncryption = param.getResponseUnEncryption();
        if (responseUnEncryption == null)
            throw BLANK_NAME_EXP.exp;

        Boolean existenceRequestBody = param.getExistenceRequestBody();
        if (existenceRequestBody == null)
            throw BLANK_NAME_EXP.exp;

        Boolean existenceResponseBody = param.getExistenceResponseBody();
        if (existenceResponseBody == null)
            throw BLANK_NAME_EXP.exp;

        Integer type = param.getType();
        assertResourceType(type, false);

        String name = param.getName();
        if (isBlank(name))
            throw BLANK_NAME_EXP.exp;

        String description = param.getDescription();
        if (isBlank(description))
            throw BLANK_DESC_EXP.exp;

        Long stamp = TIME_STAMP_GETTER.get();

        Resource resource = new Resource();

        resource.setRequestMethod(requestMethod.toUpperCase());
        resource.setModule(module.toLowerCase());
        resource.setUri(uri.toLowerCase());
        resource.setAuthenticate(authenticate);
        resource.setRequestUnDecryption(requestUnDecryption);
        resource.setResponseUnEncryption(responseUnEncryption);
        resource.setExistenceRequestBody(existenceRequestBody);
        resource.setExistenceResponseBody(existenceResponseBody);
        resource.setType(type);
        resource.setName(name);
        resource.setDescription(description);
        resource.setCreateTime(stamp);
        resource.setUpdateTime(stamp);

        return resource;
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
