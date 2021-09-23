package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface SignRewardTodayRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SignRewardTodayRelation record);

    int insertSelective(SignRewardTodayRelation record);

    SignRewardTodayRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignRewardTodayRelation record);

    int updateByPrimaryKey(SignRewardTodayRelation record);

    /**
     * 根据年份及月份查询当月每日签到奖励关联
     *
     * @param year
     * @param month
     * @return
     */
    List<SignRewardTodayRelation> listRelationByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

}