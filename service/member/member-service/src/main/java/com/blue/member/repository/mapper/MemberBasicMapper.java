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

    MemberBasic selectByPhone(@Param("phone") String phone);

    MemberBasic selectByEmail(@Param("email") String email);

    MemberBasic selectByName(@Param("name") String name);

    List<MemberBasic> selectByCondition(@Param("memberCondition") MemberCondition memberCondition);

}