package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Article;


/**
 * article mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface ArticleMapper {

    int insert(Article record);

    int insertSelective(Article record);

    int updateByPrimaryKey(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int deleteByPrimaryKey(Long id);

    Article selectByPrimaryKey(Long id);

}