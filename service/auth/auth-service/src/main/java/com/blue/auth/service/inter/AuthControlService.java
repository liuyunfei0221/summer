package com.blue.auth.service.inter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.*;
import com.blue.basic.model.common.Access;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AuthControlService {

    /**
     * session
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> insertSession(ServerRequest serverRequest);

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> deleteSession(ServerRequest serverRequest);

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> deleteSessions(ServerRequest serverRequest);

    /**
     * refresh jwt by refresh token
     *
     * @param refresh
     * @return
     */
    Mono<MemberAccess> refreshAccessByRefresh(String refresh);

    /**
     * operator's role level must higher than target member role level
     *
     * @param targetMemberId
     * @param operatorMemberId
     * @return
     */
    Boolean validateRoleLevelForOperate(Long targetMemberId, Long operatorMemberId);

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
     * invalid member auth by member id
     *
     * @param memberId
     * @param operatorId
     * @return
     */
    Mono<Boolean> invalidateAuthByMemberId(Long memberId, Long operatorId);

    /**
     * invalid local auth by key id
     *
     * @param keyId
     * @return
     */
    Mono<Boolean> invalidateLocalAccessByKeyId(String keyId);

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    Mono<Void> refreshSystemAuthorityInfos();

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * init auth info for a new member
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
    MemberBasicInfo insertCredential(CredentialSettingUpParam credentialSettingUpParam, Access access);

    /**
     * update exist credential
     *
     * @param credentialModifyParam
     * @param access
     * @return
     */
    MemberBasicInfo updateCredential(CredentialModifyParam credentialModifyParam, Access access);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    Mono<Boolean> refreshMemberRoleByIds(Long memberId, List<Long> roleIds);

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<RoleManagerInfo> updateDefaultRole(Long id, Long operatorId);

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
     * refresh member sec key by access
     *
     * @param access
     * @return
     */
    Mono<String> refreshSecKeyByAccess(Access access);

    /**
     * insert security question
     *
     * @param securityQuestionInsertParam
     * @param memberId
     * @return
     */
    Mono<Boolean> insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId);

    /**
     * insert security question batch
     *
     * @param securityQuestionInsertParams
     * @param memberId
     * @return
     */
    Mono<Boolean> insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId);

    /**
     * select member's authority by access
     *
     * @param access
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByAccess(Access access);

    /**
     * select member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByMemberId(Long memberId);

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    Mono<MemberAuthority> getAuthorityByAccess(Access access);

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberAuthority> getAuthorityByMemberId(Long memberId);

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
     * add authority base on member / insert member-role-relations
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> insertAuthorityByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId);

    /**
     * update authority base on member / update member-role-relations
     *
     * @param memberRoleRelationUpdateParam
     * @param operatorId
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> updateAuthoritiesByMember(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam, Long operatorId);

    /**
     * delete authority base on member / delete member-role-relations
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> deleteAuthorityByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId);

    /**
     * update authority base on role / generate role-resource-relations sync with trans / not support for manager
     *
     * @param roleResRelationParam
     * @return
     */
    AuthorityBaseOnRole updateAuthorityByRoleSync(RoleResRelationParam roleResRelationParam);

    /**
     * add authority base on member / insert member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    AuthorityBaseOnRole insertAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam);

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager sync
     *
     * @param memberRoleRelationUpdateParam
     * @return
     */
    List<AuthorityBaseOnRole> updateAuthoritiesByMemberSync(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam);

    /**
     * delete authority base on member / delete member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    AuthorityBaseOnRole deleteAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam);

    /**
     * select security question mono by member id
     *
     * @param memberId
     * @param operatorId
     * @return
     */
    Mono<MemberSecurityInfo> selectSecurityInfoByMemberId(Long memberId, Long operatorId);

}
