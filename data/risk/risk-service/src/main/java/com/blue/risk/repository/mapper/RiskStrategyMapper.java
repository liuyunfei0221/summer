package com.blue.risk.repository.mapper;

import com.blue.risk.model.RiskStrategyCondition;
import com.blue.risk.repository.entity.RiskStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiskStrategyMapper {

    int insert(RiskStrategy record);

    int insertSelective(RiskStrategy record);

    int updateByPrimaryKey(RiskStrategy record);

    int updateByPrimaryKeySelective(RiskStrategy record);

    int deleteByPrimaryKey(Long id);

    RiskStrategy selectByPrimaryKey(Long id);

    RiskStrategy selectByType(@Param("type") Integer type);

    List<RiskStrategy> select();

    List<RiskStrategy> selectByIds(@Param("ids") List<Long> ids);

    List<RiskStrategy> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("riskStrategyCondition") RiskStrategyCondition riskStrategyCondition);

    Long countByCondition(@Param("riskStrategyCondition") RiskStrategyCondition riskStrategyCondition);

}