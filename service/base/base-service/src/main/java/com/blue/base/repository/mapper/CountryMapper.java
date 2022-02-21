package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Country;

/**
 * country mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface CountryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Country record);

    int insertSelective(Country record);

    Country selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Country record);

    int updateByPrimaryKey(Country record);
}