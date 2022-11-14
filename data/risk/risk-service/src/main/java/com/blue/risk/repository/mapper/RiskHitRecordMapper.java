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

    int deleteByPrimaryKey(Long id);

    int insert(RiskHitRecord record);

    void insertBatch(@Param("list") List<RiskHitRecord> list);

    int insertSelective(RiskHitRecord record);

    RiskHitRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RiskHitRecord record);

    int updateByPrimaryKey(RiskHitRecord record);

    List<RiskHitRecord> selectByRowsAndSearchAfter(@Param("rows") long rows, @Param("searchAfter") Long searchAfter);

    List<RiskHitRecord> selectBySearchAfterAndCondition(@Param("rows") Long rows, @Param("riskHitRecordCondition") RiskHitRecordCondition riskHitRecordCondition, @Param("column") String column, @Param("comparison") String comparison, @Param("searchAfter") Long searchAfter);

}