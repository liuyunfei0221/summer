package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Country;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * country dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface CountryMapper {

    int insert(Country record);

    int insertSelective(Country record);

    int updateByPrimaryKey(Country record);

    int updateByPrimaryKeySelective(Country record);

    int deleteByPrimaryKey(Long id);

    Country selectByPrimaryKey(Long id);

    List<Country> select();

    List<Country> selectByIds(@Param("ids") List<Long> ids);

}