package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * area dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface AreaMapper {

    int insert(Area record);

    int insertSelective(Area record);

    int updateByPrimaryKey(Area record);

    int updateByPrimaryKeySelective(Area record);

    int deleteByPrimaryKey(Long id);

    Area selectByPrimaryKey(Long id);

    List<Area> select();

    List<Area> selectByCountryId(@Param("countryId") Long countryId);

    List<Area> selectByStateId(@Param("stateId") Long stateId);

    List<Area> selectByCityId(@Param("cityId") Long cityId);

    List<Area> selectByIds(@Param("ids") List<Long> ids);
}