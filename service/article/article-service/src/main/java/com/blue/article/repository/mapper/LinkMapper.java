package com.blue.article.repository.mapper;

import com.blue.article.repository.entity.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * link mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface LinkMapper {

    int insert(Link record);

    int insertSelective(Link record);

    int updateByPrimaryKey(Link record);

    int updateByPrimaryKeySelective(Link record);

    int deleteByPrimaryKey(Long id);

    Link selectByPrimaryKey(Long id);

    void insertBatch(@Param("list") List<Link> list);

    List<Link> selectBySubIdAndSubType(@Param("subId") Long subId, @Param("subType") Integer subType);
}