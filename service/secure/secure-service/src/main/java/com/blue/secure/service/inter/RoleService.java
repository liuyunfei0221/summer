package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Role;

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
     * get role by role id
     *
     * @param id
     * @return
     */
    Optional<Role> getRoleById(Long id);

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    Optional<Long> getRoleIdByMemberId(Long memberId);

    /**
     * get role by member id
     *
     * @param memberId
     * @return
     */
    Optional<Role> getRoleByMemberId(Long memberId);

    /**
     * select all roles
     *
     * @return
     */
    List<Role> selectRole();

    /**
     * select roles by ids
     *
     * @param ids
     * @return
     */
    List<Role> selectRoleByIds(List<Long> ids);

    /**
     * get default role
     *
     * @return
     */
    Role getDefaultRole();

}
