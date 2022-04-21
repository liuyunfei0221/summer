package com.blue.media.repository.mapper;

import com.blue.media.model.AttachmentCondition;
import com.blue.media.repository.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * attachment dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface AttachmentMapper {

    int insert(Attachment record);

    int insertSelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    int updateByPrimaryKeySelective(Attachment record);

    int deleteByPrimaryKey(Long id);

    Attachment selectByPrimaryKey(Long id);

    int insertBatch(@Param("list") List<Attachment> list);

    List<Attachment> selectByIds(@Param("ids") List<Long> ids);

    List<Attachment> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("attachmentCondition") AttachmentCondition attachmentCondition);

    Long countByCondition(@Param("attachmentCondition") AttachmentCondition attachmentCondition);

    List<Attachment> selectByLimitAndMemberId(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberId") Long memberId);

    Long countByMemberId(@Param("memberId") Long memberId);

}