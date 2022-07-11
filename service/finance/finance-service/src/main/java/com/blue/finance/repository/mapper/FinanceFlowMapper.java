package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.FinanceFlow;

/**
 * finance flow dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface FinanceFlowMapper {

    int insert(FinanceFlow record);

    int insertSelective(FinanceFlow record);

    int updateByPrimaryKey(FinanceFlow record);

    int updateByPrimaryKeySelective(FinanceFlow record);

    int deleteByPrimaryKey(Long id);

    FinanceFlow selectByPrimaryKey(Long id);

}