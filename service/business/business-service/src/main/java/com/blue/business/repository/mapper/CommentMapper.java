package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Comment;

/**
 * comment mapper
 *
 * @author DarkBlue
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