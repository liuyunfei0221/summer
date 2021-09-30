package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * resource mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface ResourceMapper {

    /**
     * select all resources
     *
     * @return
     */
    List<Resource> listResource();

    /**
     * select resources by ids
     *
     * @param ids
     * @return
     */
    List<Resource> listResourceByIds(@Param("ids") List<Long> ids);

}