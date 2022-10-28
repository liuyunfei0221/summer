package com.blue.agreement.repository.mapper;

import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.repository.entity.Agreement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * agreement dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface AgreementMapper {

    int insert(Agreement record);

    int insertSelective(Agreement record);

    int updateByPrimaryKey(Agreement record);

    int updateByPrimaryKeySelective(Agreement record);

    int deleteByPrimaryKey(Long id);

    Agreement selectByPrimaryKey(Long id);

    Agreement selectNewestByType(@Param("type") Integer type);

    List<Agreement> selectByIds(@Param("ids") List<Long> ids);

    List<Agreement> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("agreementCondition") AgreementCondition agreementCondition);

    Long countByCondition(@Param("agreementCondition") AgreementCondition agreementCondition);

}