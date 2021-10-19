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
     * select all role resource relation
     *
     * @return
     */
    List<RoleResRelation> selectRoleResRelation();

    /**
     * select resources ids by role id
     *
     * @return
     */
    List<Long> selectResIdsByRoleId(@Param("roleId") Long roleId);

}