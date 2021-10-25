package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Reply;

/**
 * reply mapper
 *
 * @author DarkBlue
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