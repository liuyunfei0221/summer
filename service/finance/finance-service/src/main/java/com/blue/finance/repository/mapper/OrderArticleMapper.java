package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.OrderArticle;

/**
 * order article dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface OrderArticleMapper {

    int insert(OrderArticle record);

    int insertSelective(OrderArticle record);

    int updateByPrimaryKey(OrderArticle record);

    int updateByPrimaryKeySelective(OrderArticle record);

    int deleteByPrimaryKey(Long id);

    OrderArticle selectByPrimaryKey(Long id);

}