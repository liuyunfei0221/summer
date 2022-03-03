package com.blue.member.repository.mapper;

import com.blue.member.model.MemberCondition;
import com.blue.member.repository.entity.MemberBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member basic dao
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface MemberBasicMapper {

    int insert(MemberBasic record);

    int insertSelective(MemberBasic record);

    int updateByPrimaryKey(MemberBasic record);

    int updateByPrimaryKeySelective(MemberBasic record);

    int deleteByPrimaryKey(Long id);

    MemberBasic selectByPrimaryKey(Long id);

    MemberBasic selectByPhone(@Param("phone") String phone);

    MemberBasic selectByEmail(@Param("email") String email);

    MemberBasic selectByName(@Param("name") String name);

    List<MemberBasic> select();

    List<MemberBasic> selectByIds(@Param("ids") List<Long> ids);

    List<MemberBasic> selectByLimitAndCondition(@Param("limit") Long limit, @Param("rows") Long rows, @Param("memberCondition") MemberCondition memberCondition);

    Long countByCondition(@Param("memberCondition") MemberCondition memberCondition);

}