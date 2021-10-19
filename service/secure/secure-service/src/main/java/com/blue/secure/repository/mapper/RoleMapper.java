package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * role mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "unused"})
public interface RoleMapper {

    /**
     * get role by id
     *
     * @param id
     * @return
     */
    Role selectByPrimaryKey(Long id);

    /**
     * select all roles
     *
     * @return
     */
    List<Role> selectRole();

    /**
     * select role by ids
     *
     * @param ids
     * @return
     */
    List<Role> selectRoleByIds(@Param("ids") List<Long> ids);

}