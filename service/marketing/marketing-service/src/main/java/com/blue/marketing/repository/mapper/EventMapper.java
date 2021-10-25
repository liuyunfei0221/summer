package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.Event;

/**
 * event mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface EventMapper {

    int insert(Event record);

    int insertSelective(Event record);

    int updateByPrimaryKey(Event record);

    int updateByPrimaryKeySelective(Event record);

    int deleteByPrimaryKey(Long id);

    Event selectByPrimaryKey(Long id);

}