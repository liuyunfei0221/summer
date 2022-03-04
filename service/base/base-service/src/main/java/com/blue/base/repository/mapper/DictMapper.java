package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dict dao
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface DictMapper {

    int insert(Dict record);

    int insertSelective(Dict record);

    int updateByPrimaryKey(Dict record);

    int updateByPrimaryKeySelective(Dict record);

    int deleteByPrimaryKey(Long id);

    Dict selectByPrimaryKey(Long id);

    /**
     * select all dict
     *
     * @return
     */
    List<Dict> select();

    /**
     * select dict by dict type code
     *
     * @return
     */
    List<Dict> selectByDictTypeId(@Param("dictTypeId") Long dictTypeId);
}