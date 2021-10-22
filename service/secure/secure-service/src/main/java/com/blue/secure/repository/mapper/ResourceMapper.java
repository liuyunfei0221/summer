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

    int insert(Resource record);

    int insertSelective(Resource record);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);

    int deleteByPrimaryKey(Long id);

    Resource selectByPrimaryKey(Long id);

    List<Resource> select();

    List<Resource> selectByIds(@Param("ids") List<Long> ids);

}