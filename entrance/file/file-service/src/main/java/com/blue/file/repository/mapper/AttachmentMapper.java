package com.blue.file.repository.mapper;

import com.blue.file.repository.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件持久层
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
     * 批量插入
     *
     * @param list
     */
    void insertBatch(@Param("list") List<Attachment> list);

    /**
     * 根据创建人查询文件
     *
     * @param memberId
     * @return
     */
    List<Attachment> listAttachment(@Param("memberId") Long memberId);

    /**
     * 分页查询对应成员的附件列表
     *
     * @param memberId
     * @param limit
     * @param rows
     * @return
     */
    List<Attachment> listAttachmentByLimit(@Param("memberId") Long memberId, @Param("limit") long limit, @Param("rows") long rows);

    /**
     * 统计对应成员的附件总数
     *
     * @return
     */
    Long countAttachment(@Param("memberId") Long memberId);

}