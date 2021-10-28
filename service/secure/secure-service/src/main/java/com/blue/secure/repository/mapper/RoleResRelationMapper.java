package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.RoleResRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * role - resource mapping mapper
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

    List<Long> selectRoleIdsByResId(@Param("resId") Long resId);

    List<RoleResRelation> selectByRoleId(@Param("roleId") Long roleId);

    List<RoleResRelation> selectByResId(@Param("resId") Long resId);

    List<RoleResRelation> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<RoleResRelation> selectByResIds(@Param("resIds") List<Long> resIds);

}