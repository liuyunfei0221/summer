package com.blue.article.repository.mapper;

import com.blue.article.repository.entity.Comment;

/**
 * comment mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface CommentMapper {

    int insert(Comment record);

    int insertSelective(Comment record);

    int updateByPrimaryKey(Comment record);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int deleteByPrimaryKey(Long id);

    Comment selectByPrimaryKey(Long id);
}