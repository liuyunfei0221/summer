package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * city dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface CityMapper {

    int insert(City record);

    int insertSelective(City record);

    int updateByPrimaryKey(City record);

    int updateByPrimaryKeySelective(City record);

    int deleteByPrimaryKey(Long id);

    City selectByPrimaryKey(Long id);

    List<City> select();

    List<City> selectByCountryId(@Param("countryId") Long countryId);

    List<City> selectByStateId(@Param("stateId") Long stateId);

    List<City> selectByIds(@Param("ids") List<Long> ids);
}