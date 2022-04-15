package com.blue.auth.service.inter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.*;
import com.blue.base.model.base.Access;
import com.blue.base.model.base.IdentityParam;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ControlService {

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> login(ServerRequest serverRequest);

    /**
     * refresh jwt by refresh token
     *
     * @param refresh
     * @return
     */
    Mono<MemberAccess> refreshAccess(String refresh);

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> logout(ServerRequest serverRequest);

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    Mono<Boolean> invalidateAuthByAccess(Access access);

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    Mono<Boolean> invalidateAuthByJwt(String jwt);

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> invalidateAuthByMemberId(Long memberId);

    /**
     * invalid local auth by key id
     *
     * @param keyId
     * @return
     */
    Mono<Boolean> invalidateLocalAccessByKeyId(String keyId);

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    Mono<AuthorityBaseOnRole> selectAuthorityMonoByRoleId(Long roleId);

    /**
     * get authority base on resource by res id
     *
     * @param resId
     * @return
     */
    Mono<AuthorityBaseOnResource> selectAuthorityMonoByResId(Long resId);

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    Mono<Void> refreshSystemAuthorityInfos();

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    int updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * init auth infos for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * init auth infos for a new member
     *
     * @param memberCredentialInfo
     * @param roleId
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo, Long roleId);

    /**
     * add new credential base on verify type for a member
     *
     * @param credentialSettingUpParam
     * @param access
     * @return
     */
    MemberBasicInfo credentialSettingUp(CredentialSettingUpParam credentialSettingUpParam, Access access);

    /**
     * update exist credential
     *
     * @param credentialModifyParam
     * @param access
     * @return
     */
    MemberBasicInfo credentialModify(CredentialModifyParam credentialModifyParam, Access access);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void refreshMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<Boolean> updateDefaultRole(Long id, Long operatorId);

    /**
     * update member access/password by access
     *
     * @param accessUpdateParam
     * @param access
     * @return
     */
    Mono<Boolean> updateAccessByAccess(AccessUpdateParam accessUpdateParam, Access access);

    /**
     * reset member access/password by access
     *
     * @param accessResetParam
     * @return
     */
    Mono<Boolean> resetAccessByAccess(AccessResetParam accessResetParam);

    /**
     * update member sec key by access
     *
     * @param access
     * @return
     */
    Mono<String> updateSecKeyByAccess(Access access);

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access);

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId);

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> insertRole(RoleInsertParam roleInsertParam, Long operatorId);

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> updateRole(RoleUpdateParam roleUpdateParam, Long operatorId);

    /**
     * delete a exist role
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> deleteRole(Long id, Long operatorId);

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> insertResource(ResourceInsertParam resourceInsertParam, Long operatorId);

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId);

    /**
     * delete a exist resource
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> deleteResource(Long id, Long operatorId);

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleResRelationParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> updateAuthorityByRole(RoleResRelationParam roleResRelationParam, Long operatorId);

    /**
     * update authority base on member / update member-role-relations
     *
     * @param memberRoleRelationParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> updateAuthorityByMember(MemberRoleRelationParam memberRoleRelationParam, Long operatorId);

    /**
     * invalid member auth by member id
     *
     * @param identityParam
     * @param operatorId
     * @return
     */
    Mono<Boolean> invalidateAuthByMember(IdentityParam identityParam, Long operatorId);

}
