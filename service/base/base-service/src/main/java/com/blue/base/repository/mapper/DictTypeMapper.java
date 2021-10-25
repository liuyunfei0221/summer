package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.DictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dict type mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface DictTypeMapper {

    int insert(DictType record);

    int insertSelective(DictType record);

    int updateByPrimaryKey(DictType record);

    int updateByPrimaryKeySelective(DictType record);

    int deleteByPrimaryKey(Long id);

    DictType selectByPrimaryKey(Long id);

    /**
     * select all dict type
     *
     * @return
     */
    List<DictType> select();

    /**
     * get dict type by code
     *
     * @param code
     * @return
     */
    DictType getByCode(@Param("code") String code);
}