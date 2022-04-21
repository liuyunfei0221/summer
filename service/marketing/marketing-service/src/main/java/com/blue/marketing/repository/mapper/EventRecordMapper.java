package com.blue.marketing.repository.mapper;

import com.blue.marketing.repository.entity.EventRecord;

/**
 * event record dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface EventRecordMapper {

    int insert(EventRecord record);

    int insertSelective(EventRecord record);

    int updateByPrimaryKey(EventRecord record);

    int updateByPrimaryKeySelective(EventRecord record);

    int deleteByPrimaryKey(Long id);

    EventRecord selectByPrimaryKey(Long id);

}