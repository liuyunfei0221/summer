package com.blue.file.repository.mapper;

import com.blue.file.repository.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * attachment mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "unused"})
public interface AttachmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    /**
     * insert batch
     *
     * @param list
     */
    void insertBatch(@Param("list") List<Attachment> list);

    /**
     * select attachment page by member id
     *
     * @param memberId
     * @param limit
     * @param rows
     * @return
     */
    List<Attachment> listAttachmentByLimit(@Param("memberId") Long memberId, @Param("limit") long limit, @Param("rows") long rows);

    /**
     * count attachment by member id
     *
     * @return
     */
    Long countAttachment(@Param("memberId") Long memberId);

}