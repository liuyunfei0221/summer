package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.State;

/**
 * state mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface StateMapper {

    int deleteByPrimaryKey(Long id);

    int insert(State record);

    int insertSelective(State record);

    State selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(State record);

    int updateByPrimaryKey(State record);
}