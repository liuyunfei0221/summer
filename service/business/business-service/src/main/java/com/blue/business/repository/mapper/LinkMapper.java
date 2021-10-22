package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * link mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface LinkMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);

    List<Link> selectBySubIdAndSubType(@Param("subId") Long subId, @Param("subType") Integer subType);

    void insertBatch(@Param("list") List<Link> list);
}