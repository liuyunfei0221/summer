package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * link mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface LinkMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);

    /**
     * @param subId
     * @param subType
     * @return
     */
    List<Link> listBySubIdAndSubType(@Param("subId") Long subId, @Param("subType") Integer subType);

    /**
     * @param list
     */
    void insertBatch(@Param("list") List<Link> list);
}