package com.blue.marketing.repository.mapper;

import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.repository.entity.Reward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * reward dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RewardMapper {

    int insert(Reward record);

    int insertSelective(Reward record);

    int updateByPrimaryKey(Reward record);

    int updateByPrimaryKeySelective(Reward record);

    int deleteByPrimaryKey(Long id);

    Reward selectByPrimaryKey(Long id);

    List<Reward> select();

    List<Reward> selectByIds(@Param("ids") List<Long> ids);

    List<Reward> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("rewardCondition") RewardCondition rewardCondition);

    Long countByCondition(@Param("rewardCondition") RewardCondition rewardCondition);

    Reward selectByUnique(@Param("name") String name, @Param("type") Integer type);

}