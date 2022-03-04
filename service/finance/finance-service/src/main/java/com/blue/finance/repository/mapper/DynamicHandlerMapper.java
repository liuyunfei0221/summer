package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.DynamicHandler;

import java.util.List;

/**
 * dynamic handler dao
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DynamicHandlerMapper {

    int insert(DynamicHandler record);

    int insertSelective(DynamicHandler record);

    int updateByPrimaryKey(DynamicHandler record);

    int updateByPrimaryKeySelective(DynamicHandler record);

    int deleteByPrimaryKey(Long id);

    DynamicHandler selectByPrimaryKey(Long id);

    List<DynamicHandler> select();

}