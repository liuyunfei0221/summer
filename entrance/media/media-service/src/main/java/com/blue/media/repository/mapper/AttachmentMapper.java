package com.blue.media.repository.mapper;

import com.blue.media.repository.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * attachment mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface AttachmentMapper {

    int insert(Attachment record);

    int insertSelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    int updateByPrimaryKeySelective(Attachment record);

    int deleteByPrimaryKey(Long id);

    Attachment selectByPrimaryKey(Long id);

    void insertBatch(@Param("list") List<Attachment> list);

    List<Attachment> selectByLimitAndMemberId(@Param("memberId") Long memberId, @Param("limit") long limit, @Param("rows") long rows);

    Long countByMemberId(@Param("memberId") Long memberId);

}