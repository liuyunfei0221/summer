package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.Reward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * reward mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RewardMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Reward record);

    int insertSelective(Reward record);

    Reward selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reward record);

    int updateByPrimaryKey(Reward record);

    List<Reward> selectByIds(@Param("ids") List<Long> ids);

}