package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.DictType;

import java.util.List;

/**
 * dict type mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface DictTypeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DictType record);

    int insertSelective(DictType record);

    DictType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictType record);

    int updateByPrimaryKey(DictType record);

    /**
     * select all dict type
     *
     * @return
     */
    List<DictType> listDictType();
}