package com.blue.secure.repository.mapper;

import com.blue.secure.model.RoleCondition;
import com.blue.secure.repository.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * role mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RoleMapper {

    int insert(Role record);

    int insertSelective(Role record);

    int updateByPrimaryKey(Role record);

    int updateByPrimaryKeySelective(Role record);

    int deleteByPrimaryKey(Long id);

    Role selectByPrimaryKey(Long id);

    List<Role> select();

    List<Role> selectByIds(@Param("ids") List<Long> ids);

    List<Role> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("roleCondition") RoleCondition roleCondition);

    Long countByCondition(@Param("roleCondition") RoleCondition roleCondition);

}