package com.blue.marketing.repository.mapper;

import com.blue.marketing.model.EventRecordManagerCondition;
import com.blue.marketing.repository.entity.EventRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    int insertBatch(@Param("list") List<EventRecord> list);

    List<EventRecord> selectByIds(@Param("ids") List<Long> ids);

    List<EventRecord> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("eventRecordCondition") EventRecordManagerCondition eventRecordCondition);

    Long countByCondition(@Param("eventRecordCondition") EventRecordManagerCondition eventRecordCondition);

    List<EventRecord> selectByLimitAndCreator(@Param("limit") Long limit, @Param("rows") Long rows, @Param("creator") Long creator);

    Long countByCreator(@Param("creator") Long creator);

}