package com.blue.business.repository.mapper;

import com.blue.business.repository.entity.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@SuppressWarnings("JavaDoc")
public interface LinkMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);

    /**
     * 根据主题id与主题类型查询链接信息
     *
     * @param subId
     * @param subType
     * @return
     */
    List<Link> listBySubIdAndSubType(@Param("subId") Long subId, @Param("subType") Integer subType);

    /**
     * 批量插入
     *
     * @param list
     */
    void insertBatch(@Param("list") List<Link> list);
}