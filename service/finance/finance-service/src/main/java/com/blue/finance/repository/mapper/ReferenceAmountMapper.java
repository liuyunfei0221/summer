package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.ReferenceAmount;

/**
 * reference amount dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface ReferenceAmountMapper {

    int insert(ReferenceAmount record);

    int insertSelective(ReferenceAmount record);

    int updateByPrimaryKey(ReferenceAmount record);

    int updateByPrimaryKeySelective(ReferenceAmount record);

    int deleteByPrimaryKey(Long id);

    ReferenceAmount selectByPrimaryKey(Long id);

}