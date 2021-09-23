package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.RoleResRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色资源关联关系持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface RoleResRelationMapper {

    /**
     * 获取全部角色权限关联
     *
     * @return
     */
    List<RoleResRelation> listRoleResRelation();

    /**
     * 根据角色id查询角色对应的资源id
     *
     * @return
     */
    List<Long> listResIdsByRoleId(@Param("roleId") Long roleId);

}