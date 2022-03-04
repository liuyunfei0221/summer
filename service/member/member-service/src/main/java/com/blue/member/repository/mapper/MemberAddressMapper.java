package com.blue.member.repository.mapper;

import com.blue.member.model.MemberAddressCondition;
import com.blue.member.repository.entity.MemberAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member address dao
 *
 * @author blue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberAddressMapper {

    int insert(MemberAddress record);

    int insertSelective(MemberAddress record);

    int updateByPrimaryKey(MemberAddress record);

    int updateByPrimaryKeySelective(MemberAddress record);

    int deleteByPrimaryKey(Long id);

    MemberAddress selectByPrimaryKey(Long id);

    /**
     * select by member id
     *
     * @param memberId
     * @return
     */
    List<MemberAddress> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * count by member id
     *
     * @param memberId
     * @memberId
     */
    Long countByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<MemberAddress> selectByIds(@Param("ids") List<Long> ids);

    /**
     * select by limit and condition
     *
     * @param limit
     * @param rows
     * @param memberAddressCondition
     * @return
     */
    List<MemberAddress> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberAddressCondition") MemberAddressCondition memberAddressCondition);

    /**
     * count by condition
     *
     * @param memberAddressCondition
     * @return
     */
    Long countByCondition(@Param("memberAddressCondition") MemberAddressCondition memberAddressCondition);

}