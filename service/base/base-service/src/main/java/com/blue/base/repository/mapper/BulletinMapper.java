package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Bulletin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface BulletinMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Bulletin record);

    int insertSelective(Bulletin record);

    Bulletin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bulletin record);

    int updateByPrimaryKey(Bulletin record);

    /**
     * 根据条件查询对应数量公告
     *
     * @param type
     * @param status
     * @param limit
     * @return
     */
    List<Bulletin> listBulletin(@Param("type") Integer type,
                                @Param("status") Integer status, @Param("limit") Long limit);

}