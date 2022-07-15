package com.blue.basic.repository.mapper;

import com.blue.basic.repository.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * city dao
 *
 * @author liuyunfei
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