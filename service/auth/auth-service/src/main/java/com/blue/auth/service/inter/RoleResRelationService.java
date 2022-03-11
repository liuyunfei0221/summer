package com.blue.auth.service.inter;

import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.api.model.AuthorityBaseOnResource;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.ResourceUpdateParam;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleUpdateParam;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * role resource relation service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface RoleResRelationService {

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
    Mono<List<Long>> selectResIdsMonoByRoleId(Long roleId);

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<Resource>> selectResMonoByRoleId(Long roleId);

    /**
     * select role ids by resource id
     *
     * @param resId
     * @return
     */
    Mono<List<Long>> selectRoleIdsMonoByResId(Long resId);

    /**
     * select role by resource id
     *
     * @param resId
     * @return
     */
    Mono<List<Role>> selectRoleMonoByResId(Long resId);

    /**
     * select relation by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId);

    /**
     * select relation by limit and role id
     *
     * @param roleId
     * @param limit
     * @param rows
     * @return
     */
    List<RoleResRelation> selectRelationByRowsAndRoleId(Long roleId, Long limit, Long rows);

    /**
     * count relation by role id
     *
     * @param roleId
     * @return
     */
    long countRelationByRoleId(Long roleId);

    /**
     * select relation by limit and resource id
     *
     * @param resId
     * @param limit
     * @param rows
     * @return
     */
    List<RoleResRelation> selectRelationByRowsAndResId(Long resId, Long limit, Long rows);

    /**
     * count relation by resource id
     *
     * @param resId
     * @return
     */
    long countRelationByResId(Long resId);

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

    /**
     * get default role
     *
     * @return
     */
    Role getDefaultRole();

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     */
    void updateDefaultRole(Long id, Long operatorId);

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId);

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId);

    /**
     * delete a exist role
     *
     * @param id
     * @param operatorId
     * @return
     */
    RoleInfo deleteRole(Long id, Long operatorId);

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId);

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId);

    /**
     * delete a exist resource
     *
     * @param id
     * @param operatorId
     * @return
     */
    ResourceInfo deleteResource(Long id, Long operatorId);

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
     * delete relation by resource id
     *
     * @param resId
     * @return
     */
    int deleteRelationByResId(Long resId);

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleId
     * @param resIds
     * @param operatorId
     * @return
     */
    AuthorityBaseOnRole updateAuthorityByRole(Long roleId, List<Long> resIds, Long operatorId);

}
