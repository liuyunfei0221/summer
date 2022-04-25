package com.blue.auth.repository.mapper;

import com.blue.auth.repository.entity.RoleResRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * role - resource mapping mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface RoleResRelationMapper {

    int insert(RoleResRelation record);

    int insertSelective(RoleResRelation record);

    int insertBatch(@Param("list") List<RoleResRelation> list);

    int updateByPrimaryKey(RoleResRelation record);

    int updateByPrimaryKeySelective(RoleResRelation record);

    int deleteByPrimaryKey(Long id);

    int deleteByRoleId(Long roleId);

    int deleteByResId(Long resId);

    RoleResRelation selectByPrimaryKey(Long id);

    List<RoleResRelation> select();

    List<Long> selectResIdsByRoleId(@Param("roleId") Long roleId);

    List<Long> selectRoleIdsByResId(@Param("resId") Long resId);

    List<RoleResRelation> selectByRoleId(@Param("roleId") Long roleId);

    List<RoleResRelation> selectByResId(@Param("resId") Long resId);

    List<RoleResRelation> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<RoleResRelation> selectRelationByRowsAndRoleId(@Param("roleId") Long roleId, @Param("limit") Long limit, @Param("rows") Long rows);

    Long countByRoleId(@Param("roleId") Long roleId);

    List<RoleResRelation> selectRelationByRowsAndResId(@Param("resId") Long resId, @Param("limit") Long limit, @Param("rows") Long rows);

    Long countByResId(@Param("resId") Long resId);

    List<RoleResRelation> selectByResIds(@Param("resIds") List<Long> resIds);

    RoleResRelation selectExistByRoleIdAndResId(@Param("roleId") Long roleId, @Param("resId") Long resId);

}