package com.blue.auth.repository.mapper;

import com.blue.auth.model.ResourceCondition;
import com.blue.auth.repository.entity.Resource;
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

    int updateByPrimaryKey(Resource record);

    int updateByPrimaryKeySelective(Resource record);

    int deleteByPrimaryKey(Long id);

    Resource selectByPrimaryKey(Long id);

    List<Resource> select();

    List<Resource> selectByIds(@Param("ids") List<Long> ids);

    List<Resource> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("resourceCondition") ResourceCondition resourceCondition);

    Long countByCondition(@Param("resourceCondition") ResourceCondition resourceCondition);

    Resource selectByUnique(@Param("requestMethod") String requestMethod, @Param("module") String module, @Param("uri") String uri);

    Resource selectByName(@Param("name") String name);

}