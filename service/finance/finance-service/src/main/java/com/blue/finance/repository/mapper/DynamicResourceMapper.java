package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.DynamicResource;

import java.util.List;

/**
 * dynamic resource dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DynamicResourceMapper {

    int insert(DynamicResource record);

    int insertSelective(DynamicResource record);

    int updateByPrimaryKey(DynamicResource record);

    int updateByPrimaryKeySelective(DynamicResource record);

    int deleteByPrimaryKey(Long id);

    DynamicResource selectByPrimaryKey(Long id);

    List<DynamicResource> select();
}