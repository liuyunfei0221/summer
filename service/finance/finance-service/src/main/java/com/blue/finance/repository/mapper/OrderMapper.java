package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.Order;

/**
 * order dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface OrderMapper {

    int insert(Order record);

    int insertSelective(Order record);

    int updateByPrimaryKey(Order record);

    int updateByPrimaryKeySelective(Order record);

    int deleteByPrimaryKey(Long id);

    Order selectByPrimaryKey(Long id);

}