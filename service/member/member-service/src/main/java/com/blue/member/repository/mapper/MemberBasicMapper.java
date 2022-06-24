package com.blue.member.repository.mapper;

import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member basic dao
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface MemberBasicMapper {

    int insert(MemberBasic record);

    int insertSelective(MemberBasic record);

    int updateByPrimaryKey(MemberBasic record);

    int updateByPrimaryKeySelective(MemberBasic record);

    int deleteByPrimaryKey(Long id);

    int updateIcon(@Param("id") Long id, @Param("icon") String icon, @Param("updateTime") Long updateTime);

    int updateQrCode(@Param("id") Long id, @Param("qrCode") String qrCode, @Param("updateTime") Long updateTime);

    int updateProfile(@Param("id") Long id, @Param("profile") String profile, @Param("updateTime") Long updateTime);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateTime") Long updateTime);

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
    List<MemberBasic> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberBasicCondition") MemberBasicCondition memberBasicCondition);

    /**
     * count by condition
     *
     * @param memberBasicCondition
     * @return
     */
    Long countByCondition(@Param("memberBasicCondition") MemberBasicCondition memberBasicCondition);

}