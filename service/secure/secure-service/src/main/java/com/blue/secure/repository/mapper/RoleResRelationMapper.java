package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.RoleResRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色资源关联关系持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface RoleResRelationMapper {

    int insert(RoleResRelation record);

    int insertSelective(RoleResRelation record);

    int updateByPrimaryKey(RoleResRelation record);

    int updateByPrimaryKeySelective(RoleResRelation record);

    int deleteByPrimaryKey(Long id);

    RoleResRelation selectByPrimaryKey(Long id);

    List<RoleResRelation> select();

    List<Long> selectResIdsByRoleId(@Param("roleId") Long roleId);

}