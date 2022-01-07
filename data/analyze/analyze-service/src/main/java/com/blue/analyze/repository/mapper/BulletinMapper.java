package com.blue.analyze.repository.mapper;

import com.blue.analyze.repository.entity.Bulletin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * test mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface BulletinMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Bulletin record);

    int insertSelective(Bulletin record);

    Bulletin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bulletin record);

    int updateByPrimaryKey(Bulletin record);

    List<Bulletin> select(@Param("type") Integer type,
                          @Param("status") Integer status, @Param("limit") Long limit);

}