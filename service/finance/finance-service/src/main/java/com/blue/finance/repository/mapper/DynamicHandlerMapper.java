package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.DynamicHandler;

import java.util.List;

@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DynamicHandlerMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DynamicHandler record);

    int insertSelective(DynamicHandler record);

    DynamicHandler selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DynamicHandler record);

    int updateByPrimaryKey(DynamicHandler record);

    /**
     * 获取全部动态处理器
     *
     * @return
     */
    List<DynamicHandler> listDynamicHandler();

}