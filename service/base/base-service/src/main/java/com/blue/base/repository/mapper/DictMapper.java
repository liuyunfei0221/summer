package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dict mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface DictMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Dict record);

    int insertSelective(Dict record);

    Dict selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dict record);

    int updateByPrimaryKey(Dict record);

    /**
     * select all dict
     *
     * @return
     */
    List<Dict> listDict();

    /**
     * select dict by dict type code
     *
     * @return
     */
    List<Dict> listDictByDictTypeId(@Param("dictTypeId") Long dictTypeId);
}