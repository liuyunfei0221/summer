package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.DynamicHandler;

import java.util.List;

/**
 * dynamic handler mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DynamicHandlerMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DynamicHandler record);

    int insertSelective(DynamicHandler record);

    DynamicHandler selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DynamicHandler record);

    int updateByPrimaryKey(DynamicHandler record);

    /**
     * list all dynamic handler
     *
     * @return
     */
    List<DynamicHandler> listDynamicHandler();

}