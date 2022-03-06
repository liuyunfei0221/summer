package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member detail dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberDetailMapper {

    int insert(MemberDetail record);

    int insertSelective(MemberDetail record);

    int updateByPrimaryKey(MemberDetail record);

    int updateByPrimaryKeySelective(MemberDetail record);

    int deleteByPrimaryKey(Long id);

    MemberDetail selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    MemberDetail selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<MemberDetail> selectByIds(@Param("ids") List<Long> ids);

}