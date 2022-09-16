package com.blue.member.repository.mapper;

import com.blue.member.model.MemberDetailCondition;
import com.blue.member.repository.entity.MemberDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member detail dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberDetailMapper {

    int insert(MemberDetail record);

    int insertSelective(MemberDetail record);

    int updateByPrimaryKey(MemberDetail record);

    int updateByPrimaryKeySelective(MemberDetail record);

    int deleteByPrimaryKey(Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateTime") Long updateTime);

    MemberDetail selectByPrimaryKey(Long id);

    /**
     * select member id by id
     *
     * @param id
     * @return
     */
    Long selectMemberIdByPrimaryKey(@Param("id") Long id);

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

    /**
     * select by member ids
     *
     * @param memberIds
     * @return
     */
    List<MemberDetail> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    /**
     * select by limit and condition
     *
     * @param limit
     * @param rows
     * @param memberDetailCondition
     * @return
     */
    List<MemberDetail> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberDetailCondition") MemberDetailCondition memberDetailCondition);

    /**
     * count by condition
     *
     * @param memberDetailCondition
     * @return
     */
    Long countByCondition(@Param("memberDetailCondition") MemberDetailCondition memberDetailCondition);

}