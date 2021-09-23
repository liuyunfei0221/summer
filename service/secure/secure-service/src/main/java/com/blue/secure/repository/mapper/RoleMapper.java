package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Role;

import java.util.List;

/**
 * 角色持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "unused"})
public interface RoleMapper {

    /**
     * 根据角色id获取角色信息
     *
     * @param id
     * @return
     */
    Role selectByPrimaryKey(Long id);

    /**
     * 获取全部角色
     *
     * @return
     */
    List<Role> listRoles();

}