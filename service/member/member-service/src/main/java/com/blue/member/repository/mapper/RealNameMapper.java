package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.RealName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * real name dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface RealNameMapper {

    int insert(RealName record);

    int insertSelective(RealName record);

    int updateByPrimaryKey(RealName record);

    int updateByPrimaryKeySelective(RealName record);

    int deleteByPrimaryKey(Long id);

    RealName selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    RealName selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<RealName> selectByIds(@Param("ids") List<Long> ids);

}