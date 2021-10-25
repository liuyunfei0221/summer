package com.blue.portal.repository.mapper;

import com.blue.portal.repository.entity.Bulletin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * bulletin mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface BulletinMapper {

    int insert(Bulletin record);

    int insertSelective(Bulletin record);

    int updateByPrimaryKey(Bulletin record);

    int updateByPrimaryKeySelective(Bulletin record);

    int deleteByPrimaryKey(Long id);

    Bulletin selectByPrimaryKey(Long id);

    List<Bulletin> select(@Param("type") Integer type,
                          @Param("status") Integer status, @Param("rows") Long rows);

}