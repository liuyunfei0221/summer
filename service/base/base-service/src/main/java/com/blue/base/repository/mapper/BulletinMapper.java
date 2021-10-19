package com.blue.base.repository.mapper;

import com.blue.base.repository.entity.Bulletin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * test bulletin mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "unused"})
public interface BulletinMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Bulletin record);

    int insertSelective(Bulletin record);

    Bulletin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bulletin record);

    int updateByPrimaryKey(Bulletin record);

    /**
     * @param type
     * @param status
     * @param limit
     * @return
     */
    List<Bulletin> selectBulletin(@Param("type") Integer type,
                                @Param("status") Integer status, @Param("limit") Long limit);

}