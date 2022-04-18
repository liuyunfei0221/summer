package com.blue.article.repository.mapper;

import com.blue.article.repository.entity.Reply;

/**
 * reply mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface ReplyMapper {

    int insert(Reply record);

    int insertSelective(Reply record);

    int updateByPrimaryKey(Reply record);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKeyWithBLOBs(Reply record);

    int deleteByPrimaryKey(Long id);

    Reply selectByPrimaryKey(Long id);
}