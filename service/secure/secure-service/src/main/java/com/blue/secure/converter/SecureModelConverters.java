package com.blue.secure.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;

import java.util.function.Function;

import static com.blue.base.common.base.BlueCheck.isBlank;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.*;
import static com.blue.base.constant.base.Default.NOT_DEFAULT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;

/**
 * model converters in secure project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class SecureModelConverters {

    /**
     * role insert param -> role
     */
    public static final Function<RoleInsertParam, Role> ROLE_INSERT_PARAM_2_ROLE_CONVERTER = param -> {
        if (param == null)
            throw new BlueException(EMPTY_PARAM);

        String name = param.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = param.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

        Integer level = param.getLevel();
        if (level == null || level < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "level can't be null or less than 1");

        Long stamp = TIME_STAMP_GETTER.get();

        Role role = new Role();

        role.setName(name);
        role.setDescription(description);
        role.setLevel(level);
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
            throw new BlueException(EMPTY_PARAM);

        return new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault());
    };

    /**
     * resource insert param -> resource
     */
    public static final Function<ResourceInsertParam, Resource> RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER = param -> {
        if (param == null)
            throw new BlueException(EMPTY_PARAM);

        String requestMethod = param.getRequestMethod();
        assertHttpMethod(requestMethod, false);

        String module = param.getModule();
        if (isBlank(module))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "module can't be blank");

        String uri = param.getUri();
        if (isBlank(uri))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "uri can't be blank");

        Boolean authenticate = param.getAuthenticate();
        if (authenticate == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "authenticate can't be null");

        Boolean requestUnDecryption = param.getRequestUnDecryption();
        if (requestUnDecryption == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "requestUnDecryption can't be null");

        Boolean responseUnEncryption = param.getResponseUnEncryption();
        if (responseUnEncryption == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "responseUnEncryption can't be null");

        Boolean existenceRequestBody = param.getExistenceRequestBody();
        if (existenceRequestBody == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceRequestBody can't be null");

        Boolean existenceResponseBody = param.getExistenceResponseBody();
        if (existenceResponseBody == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceResponseBody can't be null");

        Integer type = param.getType();
        assertResourceType(type, false);

        String name = param.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = param.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

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
            throw new BlueException(EMPTY_PARAM);

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(),
                resource.getAuthenticate(), resource.getRequestUnDecryption(), resource.getResponseUnEncryption(),
                resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription());
    };

}
