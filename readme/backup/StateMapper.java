package com.blue.basic.repository.mapper;

import com.blue.basic.repository.entity.State;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * state dao
 *
 * @author liuyunfei
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

    List<State> selectByIds(@Param("ids") List<Long> ids);

}