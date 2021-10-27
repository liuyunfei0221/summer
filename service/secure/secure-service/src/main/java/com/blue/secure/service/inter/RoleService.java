package com.blue.secure.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.RoleCondition;
import com.blue.secure.repository.entity.Role;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * role service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RoleService {

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
     * get role by role id
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
