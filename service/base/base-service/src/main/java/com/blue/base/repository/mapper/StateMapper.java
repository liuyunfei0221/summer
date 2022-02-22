package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.State;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * state mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface StateMapper {

    int insert(State record);

    int insertSelective(State record);

    int updateByPrimaryKey(State record);

    int updateByPrimaryKeySelective(State record);

    int deleteByPrimaryKey(Long id);

    State selectByPrimaryKey(Long id);

    List<State> select();

    List<State> selectByCountryId(@Param("countryId") Long countryId);

}