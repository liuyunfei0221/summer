package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.DynamicResource;

import java.util.List;

/**
 * dynamic resource mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DynamicResourceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DynamicResource record);

    int insertSelective(DynamicResource record);

    DynamicResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DynamicResource record);

    int updateByPrimaryKey(DynamicResource record);

    List<DynamicResource> select();
}