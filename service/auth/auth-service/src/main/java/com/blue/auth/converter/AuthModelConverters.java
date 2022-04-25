package com.blue.auth.converter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.base.model.exps.BlueException;

import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.*;
import static com.blue.base.constant.base.Default.NOT_DEFAULT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;

/**
 * model converters in auth project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
public final class AuthModelConverters {

    /**
     * role insert param -> role
     */
    public static final Function<RoleInsertParam, Role> ROLE_INSERT_PARAM_2_ROLE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);

        String name = param.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = param.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

        Integer level = param.getLevel();
        if (isNull(level) || level < 1)
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
        if (isNull(role))
            throw new BlueException(EMPTY_PARAM);

        return new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault());
    };

    /**
     * role -> role manager indo
     *
     * @param role
     * @param creatorName
     * @param updaterName
     * @return
     */
    public static RoleManagerInfo roleToRoleManagerInfo(Role role, String creatorName, String updaterName) {
        if (isNull(role))
            throw new BlueException(EMPTY_PARAM);

        return new RoleManagerInfo(role.getId(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault(),
                role.getCreateTime(), role.getUpdateTime(), role.getCreator(), isNotBlank(creatorName) ? creatorName : "",
                role.getUpdater(), isNotBlank(updaterName) ? updaterName : "");
    }

    /**
     * resource insert param -> resource
     */
    public static final Function<ResourceInsertParam, Resource> RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER = param -> {
        if (isNull(param))
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
        if (isNull(authenticate))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "authenticate can't be null");

        Boolean requestUnDecryption = param.getRequestUnDecryption();
        if (isNull(requestUnDecryption))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "requestUnDecryption can't be null");

        Boolean responseUnEncryption = param.getResponseUnEncryption();
        if (isNull(responseUnEncryption))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "responseUnEncryption can't be null");

        Boolean existenceRequestBody = param.getExistenceRequestBody();
        if (isNull(existenceRequestBody))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceRequestBody can't be null");

        Boolean existenceResponseBody = param.getExistenceResponseBody();
        if (isNull(existenceResponseBody))
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
        if (isNull(resource))
            throw new BlueException(EMPTY_PARAM);

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(),
                resource.getAuthenticate(), resource.getRequestUnDecryption(), resource.getResponseUnEncryption(),
                resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription());
    };

    /**
     * resource -> resource manager indo
     *
     * @param resource
     * @param creatorName
     * @param updaterName
     * @return
     */
    public static ResourceManagerInfo resourceToResourceManagerInfo(Resource resource, String creatorName, String updaterName) {
        if (isNull(resource))
            throw new BlueException(EMPTY_PARAM);

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceManagerInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(), resource.getAuthenticate(),
                resource.getRequestUnDecryption(), resource.getResponseUnEncryption(), resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription(), resource.getCreateTime(), resource.getUpdateTime(), resource.getCreator(), isNotBlank(creatorName) ? creatorName : "",
                resource.getUpdater(), isNotBlank(updaterName) ? updaterName : "");
    }

    /**
     * credential -> credential info
     */
    public static final Function<Credential, CredentialInfo> CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER = credential -> {
        if (isNull(credential))
            throw new BlueException(EMPTY_PARAM);

        return new CredentialInfo(credential.getCredential(), credential.getType(), "", credential.getStatus(), credential.getExtra());
    };


}
