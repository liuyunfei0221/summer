package com.blue.file.repository.mapper;

import com.blue.file.repository.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * attachment mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public interface AttachmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    void insertBatch(@Param("list") List<Attachment> list);

    List<Attachment> selectByLimitAndMemberId(@Param("memberId") Long memberId, @Param("limit") long limit, @Param("rows") long rows);

    Long countByMemberId(@Param("memberId") Long memberId);

}