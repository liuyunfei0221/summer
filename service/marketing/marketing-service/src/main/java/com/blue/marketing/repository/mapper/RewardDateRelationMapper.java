package com.blue.marketing.repository.mapper;

import com.blue.marketing.model.RewardDateRelationCondition;
import com.blue.marketing.repository.entity.RewardDateRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * reward and date relation dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RewardDateRelationMapper {

    int insert(RewardDateRelation record);

    int insertSelective(RewardDateRelation record);

    int updateByPrimaryKey(RewardDateRelation record);

    int updateByPrimaryKeySelective(RewardDateRelation record);

    int deleteByPrimaryKey(Long id);

    int deleteByRewardId(Long rewardId);

    RewardDateRelation selectByPrimaryKey(Long id);

    List<RewardDateRelation> selectByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

    Long countByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

    int insertBatch(@Param("list") List<RewardDateRelation> list);

    RewardDateRelation selectByUnique(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    List<RewardDateRelation> selectByIds(@Param("ids") List<Long> ids);

    List<RewardDateRelation> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("rewardDateRelationCondition") RewardDateRelationCondition rewardDateRelationCondition);

    Long countByCondition(@Param("rewardDateRelationCondition") RewardDateRelationCondition rewardDateRelationCondition);

}