package com.blue.auth.converter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.*;
import com.blue.auth.repository.entity.*;
import com.blue.basic.model.exps.BlueException;

import java.util.Map;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.getResourceTypeByIdentity;
import static com.blue.basic.constant.common.Default.NOT_DEFAULT;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Symbol.PATH_SEPARATOR;
import static java.util.Optional.ofNullable;

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
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Role role = new Role();

        role.setName(param.getName());
        role.setDescription(param.getDescription());
        role.setLevel(param.getLevel());
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

        return new RoleInfo(role.getId(), role.getType(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault());
    };

    /**
     * role -> role manager indo
     *
     * @param role
     * @param idAndMemberNameMapping
     * @return
     */
    public static RoleManagerInfo roleToRoleManagerInfo(Role role, Map<Long, String> idAndMemberNameMapping) {
        if (isNull(role))
            throw new BlueException(EMPTY_PARAM);

        return new RoleManagerInfo(role.getId(), role.getType(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault(),
                role.getCreateTime(), role.getUpdateTime(), role.getCreator(), ofNullable(idAndMemberNameMapping.get(role.getCreator())).orElse(EMPTY_DATA.value),
                role.getUpdater(), ofNullable(idAndMemberNameMapping.get(role.getUpdater())).orElse(EMPTY_DATA.value));
    }

    /**
     * resource insert param -> resource
     */
    public static final Function<ResourceInsertParam, Resource> RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Resource resource = new Resource();

        resource.setRequestMethod(param.getRequestMethod().toUpperCase());
        resource.setModule(param.getModule().toLowerCase());
        resource.setUri(param.getUri().toLowerCase());
        resource.setRelationView(param.getRelationView());
        resource.setAuthenticate(param.getAuthenticate());
        resource.setRequestUnDecryption(param.getRequestUnDecryption());
        resource.setResponseUnEncryption(param.getResponseUnEncryption());
        resource.setExistenceRequestBody(param.getExistenceRequestBody());
        resource.setExistenceResponseBody(param.getExistenceResponseBody());
        resource.setType(param.getType());
        resource.setName(param.getName().toLowerCase());
        resource.setDescription(param.getDescription().toLowerCase());
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

        return new ResourceInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(), resource.getRelationView(),
                resource.getAuthenticate(), resource.getRequestUnDecryption(), resource.getResponseUnEncryption(), resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                getResourceTypeByIdentity(resource.getType()).disc.intern(), resource.getName(), resource.getDescription());
    };

    /**
     * resource -> resource manager indo
     *
     * @param resource
     * @param idAndMemberNameMapping
     * @return
     */
    public static ResourceManagerInfo resourceToResourceManagerInfo(Resource resource, Map<Long, String> idAndMemberNameMapping) {
        if (isNull(resource))
            throw new BlueException(EMPTY_PARAM);

        String module = resource.getModule().intern();
        String relativeUri = resource.getUri().intern();

        return new ResourceManagerInfo(resource.getId(), resource.getRequestMethod().intern(), module, relativeUri, (PATH_SEPARATOR.identity.intern() + module + relativeUri).intern(), resource.getRelationView(), resource.getAuthenticate(),
                resource.getRequestUnDecryption(), resource.getResponseUnEncryption(), resource.getExistenceRequestBody(), resource.getExistenceResponseBody(), getResourceTypeByIdentity(resource.getType()).disc.intern(),
                resource.getName(), resource.getDescription(), resource.getCreateTime(), resource.getUpdateTime(), resource.getCreator(), ofNullable(idAndMemberNameMapping.get(resource.getCreator())).orElse(EMPTY_DATA.value),
                resource.getUpdater(), ofNullable(idAndMemberNameMapping.get(resource.getUpdater())).orElse(EMPTY_DATA.value));
    }

    /**
     * credential -> credential info
     */
    public static final Function<Credential, CredentialInfo> CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER = credential -> {
        if (isNull(credential))
            throw new BlueException(EMPTY_PARAM);

        return new CredentialInfo(credential.getCredential(), credential.getType(), EMPTY_DATA.value, credential.getStatus(), credential.getExtra());
    };

    /**
     * credential history -> credential history info
     */
    public static final Function<CredentialHistory, CredentialHistoryInfo> CREDENTIAL_HISTORY_2_CREDENTIAL_HISTORY_INFO_CONVERTER = credentialHistory -> {
        if (isNull(credentialHistory))
            throw new BlueException(EMPTY_PARAM);

        return new CredentialHistoryInfo(credentialHistory.getCredential(), credentialHistory.getCreateTime());
    };

    /**
     * security question insert param -> security question
     */
    public static final Function<SecurityQuestionInsertParam, SecurityQuestion> SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        SecurityQuestion securityQuestion = new SecurityQuestion();

        securityQuestion.setQuestion(param.getQuestion());
        securityQuestion.setAnswer(param.getAnswer());
        securityQuestion.setCreateTime(TIME_STAMP_GETTER.get());

        return securityQuestion;
    };

    /**
     * security question -> security question info
     */
    public static final Function<SecurityQuestion, SecurityQuestionInfo> SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER = securityQuestion -> {
        if (isNull(securityQuestion))
            throw new BlueException(EMPTY_PARAM);

        return new SecurityQuestionInfo(securityQuestion.getQuestion(), securityQuestion.getAnswer());
    };

}
