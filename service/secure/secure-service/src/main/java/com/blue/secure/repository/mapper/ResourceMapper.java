package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface ResourceMapper {

    /**
     * 获取全部资源
     *
     * @return
     */
    List<Resource> listResource();

    /**
     * 根据id批量查询资源
     *
     * @param ids
     * @return
     */
    List<Resource> listResourceByIds(@Param("ids") List<Long> ids);

}