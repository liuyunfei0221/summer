package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member business dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberBusinessMapper {

    int insert(MemberBusiness record);

    int insertSelective(MemberBusiness record);

    int updateByPrimaryKey(MemberBusiness record);

    int updateByPrimaryKeySelective(MemberBusiness record);

    int deleteByPrimaryKey(Long id);

    MemberBusiness selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    MemberBusiness selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<MemberBusiness> selectByIds(@Param("ids") List<Long> ids);

}