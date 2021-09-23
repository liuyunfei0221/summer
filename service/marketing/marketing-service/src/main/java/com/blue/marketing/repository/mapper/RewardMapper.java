package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.Reward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface RewardMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Reward record);

    int insertSelective(Reward record);

    Reward selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reward record);

    int updateByPrimaryKey(Reward record);

    /**
     * 根据主键集合批量查询奖励信息
     *
     * @param ids
     * @return
     */
    List<Reward> listRewardByIds(@Param("ids") List<Long> ids);

}