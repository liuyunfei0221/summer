package com.blue.auth.service.inter;

import com.blue.auth.api.model.AuthorityBaseOnResource;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * role resource relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused"})
public interface RoleResRelationService {

    /**
     * insert relation
     *
     * @param roleResRelation
     * @return
     */
    int insertRelation(RoleResRelation roleResRelation);

    /**
     * insert relations
     *
     * @param roleResRelations
     * @return
     */
    int insertRelationBatch(List<RoleResRelation> roleResRelations);

    /**
     * delete relation by role id
     *
     * @param roleId
     * @return
     */
    int deleteRelationByRoleId(Long roleId);

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleId
     * @param resIds
     * @param operatorId
     * @return
     */
    AuthorityBaseOnRole updateAuthorityByRole(Long roleId, List<Long> resIds, Long operatorId);

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId);

    /**
     * get authorities base on role by role ids
     *
     * @param roleIds
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByRoleIds(List<Long> roleIds);

    /**
     * get authority base on resource by res id
     *
     * @param resId
     * @return
     */
    Mono<AuthorityBaseOnResource> selectAuthorityByResId(Long resId);

    /**
     * select all role resource relation
     *
     * @return
     */
    Mono<List<RoleResRelation>> selectRelation();

    /**
     * select resources ids by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<Long>> selectResIdsByRoleId(Long roleId);

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<Resource>> selectResByRoleId(Long roleId);

    /**
     * select role ids by resource id
     *
     * @param resId
     * @return
     */
    Mono<List<Long>> selectRoleIdsByResId(Long resId);

    /**
     * select role by resource id
     *
     * @param resId
     * @return
     */
    Mono<List<Role>> selectRoleByResId(Long resId);

    /**
     * select relation by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId);

    /**
     * select relation by resource id
     *
     * @param resId
     * @return
     */
    Mono<List<RoleResRelation>> selectRelationByResId(Long resId);

    /**
     * select relation by role ids
     *
     * @param roleIds
     * @return
     */
    Mono<List<RoleResRelation>> selectRelationByRoleIds(List<Long> roleIds);

    /**
     * select relation by resource ids
     *
     * @param resIds
     * @return
     */
    Mono<List<RoleResRelation>> selectRelationByResIds(List<Long> resIds);

}
