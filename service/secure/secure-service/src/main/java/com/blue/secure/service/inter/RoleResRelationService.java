package com.blue.secure.service.inter;

import com.blue.secure.api.model.AuthorityBaseOnRole;
import com.blue.secure.model.AuthorityBaseOnResource;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.entity.RoleResRelation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * role resource relation service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RoleResRelationService {

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

}
