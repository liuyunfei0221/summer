package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.RoleResRelation;

import java.util.List;

/**
 * 角色资源关联接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RoleResRelationService {

    /**
     * 获取全部资源角色关联
     *
     * @return
     */
    List<RoleResRelation> listRoleResRelation();

    /**
     * 根据角色id查询角色对应的资源id
     *
     * @param roleId
     * @return
     */
    List<Long> listResourceIdsByRoleId(Long roleId);

}
