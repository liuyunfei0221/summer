package com.blue.secure.converter;

import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;

import java.util.function.Function;

import static com.blue.base.common.base.ConstantProcessor.getResourceTypeByIdentity;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;

/**
 * secure服务转换器
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class SecureModelConverters {

    /**
     * 角色实体转角色信息
     */
    public static final Function<Role, RoleInfo> ROLE_2_ROLE_INFO_CONVERTER = role -> {
        if (role == null)
            throw new RuntimeException("role can't be null");

        return new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getIsDefault());
    };

    /**
     * 资源实体转资源信息
     */
    public static final Function<Resource, ResourceInfo> RESOURCE_2_RESOURCE_INFO_CONVERTER = resource -> {
        if (resource == null)
            throw new RuntimeException("resource can't be null");

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(),
                resource.getAuthenticate(), resource.getPreUnDecryption(), resource.getPostUnEncryption(),
                resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription());
    };

}
