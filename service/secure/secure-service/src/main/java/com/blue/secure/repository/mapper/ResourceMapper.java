package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * resource mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface ResourceMapper {

    List<Resource> select();

    List<Resource> selectByIds(@Param("ids") List<Long> ids);

}