package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * 角色业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RoleService {

    /**
     * 根据主键查询角色信息
     *
     * @param id
     * @return
     */
    Optional<Role> getRoleById(Long id);

    /**
     * 根据成员id获取角色id
     *
     * @param memberId
     * @return
     */
    Optional<Long> getRoleIdByMemberId(Long memberId);

    /**
     * 根据成员id获取角色信息
     *
     * @param memberId
     * @return
     */
    Optional<Role> getRoleByMemberId(Long memberId);

    /**
     * 获取全部角色信息
     *
     * @return
     */
    List<Role> listRoles();

    /**
     * 查询默认角色
     *
     * @return
     */
    Role getDefaultRole();

}
