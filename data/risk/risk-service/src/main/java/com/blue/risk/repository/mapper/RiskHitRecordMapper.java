package com.blue.risk.repository.mapper;

import com.blue.risk.model.RiskHitRecordCondition;
import com.blue.risk.repository.entity.RiskHitRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * risk hit record dao
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface RiskHitRecordMapper {

    int insert(RiskHitRecord record);

    void insertBatch(@Param("list") List<RiskHitRecord> list);

    int insertSelective(RiskHitRecord record);

    int updateByPrimaryKey(RiskHitRecord record);

    int updateByPrimaryKeySelective(RiskHitRecord record);

    int deleteByPrimaryKey(Long id);

    RiskHitRecord selectByPrimaryKey(Long id);

    List<RiskHitRecord> selectBySearchAfterAndCondition(@Param("rows") Long rows, @Param("riskHitRecordCondition") RiskHitRecordCondition riskHitRecordCondition, @Param("column") String column, @Param("comparison") String comparison, @Param("searchAfter") Long searchAfter);

    long countByCondition(@Param("riskHitRecordCondition") RiskHitRecordCondition riskHitRecordCondition);

}