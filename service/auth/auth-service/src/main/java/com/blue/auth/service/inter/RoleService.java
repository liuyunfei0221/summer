package com.blue.auth.service.inter;

import com.blue.auth.repository.entity.Role;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.model.RoleCondition;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleUpdateParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * role service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RoleService {

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
     * delete role
     *
     * @param id
     * @return
     */
    RoleInfo deleteRoleById(Long id);

    /**
     * refresh default role
     *
     * @return
     */
    void refreshDefaultRole();

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
     * get role by role id
     *
     * @param id
     * @return
     */
    Optional<Role> getRoleById(Long id);

    /**
     * get role mono by role id
     *
     * @param id
     * @return
     */
    Mono<Optional<Role>> getRoleMonoById(Long id);

    /**
     * select roles by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Role>> selectRoleMonoByIds(List<Long> ids);

    /**
     * select all roles
     *
     * @return
     */
    Mono<List<Role>> selectRole();

    /**
     * select role by page and condition
     *
     * @param limit
     * @param rows
     * @param roleCondition
     * @return
     */
    Mono<List<Role>> selectRoleMonoByLimitAndCondition(Long limit, Long rows, RoleCondition roleCondition);

    /**
     * count role by condition
     *
     * @param roleCondition
     * @return
     */
    Mono<Long> countRoleMonoByCondition(RoleCondition roleCondition);

    /**
     * select role info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RoleInfo>> selectRoleInfoPageMonoByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest);

}
