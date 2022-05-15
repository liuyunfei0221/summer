package com.blue.portal.repository.mapper;

import com.blue.portal.model.BulletinCondition;
import com.blue.portal.repository.entity.Bulletin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * bulletin dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface BulletinMapper {

    int insert(Bulletin record);

    int insertSelective(Bulletin record);

    int updateByPrimaryKey(Bulletin record);

    int updateByPrimaryKeySelective(Bulletin record);

    int deleteByPrimaryKey(Long id);

    Bulletin selectByPrimaryKey(Long id);

    List<Bulletin> select();

    List<Bulletin> selectByIds(@Param("ids") List<Long> ids);

    List<Bulletin> selectAllByCondition(@Param("currentStamp") Long currentStamp, @Param("type") Integer type, @Param("status") Integer status);

    List<Bulletin> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("bulletinCondition") BulletinCondition bulletinCondition);

    Long countByCondition(@Param("bulletinCondition") BulletinCondition bulletinCondition);

}