package com.blue.member.repository.mapper;

import com.blue.member.model.MemberCondition;
import com.blue.member.repository.entity.MemberBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member basic mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface MemberBasicMapper {

    int insert(MemberBasic record);

    int insertSelective(MemberBasic record);

    int updateByPrimaryKeySelective(MemberBasic record);

    MemberBasic selectByPrimaryKey(Long id);

    MemberBasic getByPhone(@Param("phone") String phone);

    MemberBasic getByEmail(@Param("email") String email);

    MemberBasic getByName(@Param("name") String name);

    List<MemberBasic> select();

    List<MemberBasic> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberCondition") MemberCondition memberCondition);

    Long countByCondition(@Param("memberCondition") MemberCondition memberCondition);

}