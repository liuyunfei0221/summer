package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * reward and date relation mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface SignRewardTodayRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SignRewardTodayRelation record);

    int insertSelective(SignRewardTodayRelation record);

    SignRewardTodayRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignRewardTodayRelation record);

    int updateByPrimaryKey(SignRewardTodayRelation record);

    List<SignRewardTodayRelation> selectByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

}