package com.blue.member.repository.mapper;

import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member basic dao
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberBasicMapper {

    int insert(MemberBasic record);

    int insertSelective(MemberBasic record);

    int updateByPrimaryKey(MemberBasic record);

    int updateByPrimaryKeySelective(MemberBasic record);

    int deleteByPrimaryKey(Long id);

    MemberBasic selectByPrimaryKey(Long id);

    /**
     * select by phone
     *
     * @param phone
     * @return
     */
    MemberBasic selectByPhone(@Param("phone") String phone);

    /**
     * select by email
     *
     * @param email
     * @return
     */
    MemberBasic selectByEmail(@Param("email") String email);

    /**
     * select by name
     *
     * @param name
     * @return
     */
    MemberBasic selectByName(@Param("name") String name);

    /**
     * select by ids
     *
     * @param ids
     * @return
     */
    List<MemberBasic> selectByIds(@Param("ids") List<Long> ids);

    /**
     * select by limit and condition
     *
     * @param limit
     * @param rows
     * @param memberBasicCondition
     * @return
     */
    List<MemberBasic> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberCondition") MemberBasicCondition memberBasicCondition);

    /**
     * count by condition
     *
     * @param memberBasicCondition
     * @return
     */
    Long countByCondition(@Param("memberCondition") MemberBasicCondition memberBasicCondition);

}