package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.Refund;

/**
 * refund dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface RefundMapper {

    int insert(Refund record);

    int insertSelective(Refund record);

    int updateByPrimaryKey(Refund record);

    int updateByPrimaryKeySelective(Refund record);

    int deleteByPrimaryKey(Long id);

    Refund selectByPrimaryKey(Long id);

}