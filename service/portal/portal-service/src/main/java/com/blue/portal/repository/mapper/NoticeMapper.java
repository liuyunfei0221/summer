package com.blue.portal.repository.mapper;

import com.blue.portal.model.NoticeCondition;
import com.blue.portal.repository.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * notice dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface NoticeMapper {

    int insert(Notice record);

    int insertSelective(Notice record);

    int updateByPrimaryKey(Notice record);

    int updateByPrimaryKeySelective(Notice record);

    int deleteByPrimaryKey(Long id);

    Notice selectByPrimaryKey(Long id);

    Notice selectByType(@Param("type") Integer type);

    List<Notice> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("noticeCondition") NoticeCondition noticeCondition);

    Long countByCondition(@Param("noticeCondition") NoticeCondition noticeCondition);

}