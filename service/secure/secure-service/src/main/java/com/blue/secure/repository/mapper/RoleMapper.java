package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Role;

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
    List<Role> listRoles();

}