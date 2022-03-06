package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberRealName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member real name dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberRealNameMapper {

    int insert(MemberRealName record);

    int insertSelective(MemberRealName record);

    int updateByPrimaryKey(MemberRealName record);

    int updateByPrimaryKeySelective(MemberRealName record);

    int deleteByPrimaryKey(Long id);

    MemberRealName selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    MemberRealName selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<MemberRealName> selectByIds(@Param("ids") List<Long> ids);

}