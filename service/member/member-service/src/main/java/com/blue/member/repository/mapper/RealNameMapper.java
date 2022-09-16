package com.blue.member.repository.mapper;

import com.blue.member.model.RealNameCondition;
import com.blue.member.repository.entity.RealName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * real name dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface RealNameMapper {

    int insert(RealName record);

    int insertSelective(RealName record);

    int updateByPrimaryKey(RealName record);

    int updateByPrimaryKeySelective(RealName record);

    int deleteByPrimaryKey(Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateTime") Long updateTime);

    RealName selectByPrimaryKey(Long id);

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
    RealName selectByMemberId(@Param("memberId") Long memberId);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<RealName> selectByIds(@Param("ids") List<Long> ids);

    /**
     * select by member ids
     *
     * @param memberIds
     * @return
     */
    List<RealName> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    /**
     * select by limit and condition
     *
     * @param limit
     * @param rows
     * @param realNameCondition
     * @return
     */
    List<RealName> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("realNameCondition") RealNameCondition realNameCondition);

    /**
     * count by condition
     *
     * @param realNameCondition
     * @return
     */
    Long countByCondition(@Param("realNameCondition") RealNameCondition realNameCondition);

}