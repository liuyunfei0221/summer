package com.blue.auth.repository.mapper;

import com.blue.auth.repository.entity.SecurityQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * security question dao
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface SecurityQuestionMapper {

    int insert(SecurityQuestion record);

    int insertSelective(SecurityQuestion record);

    int updateByPrimaryKey(SecurityQuestion record);

    int updateByPrimaryKeySelective(SecurityQuestion record);

    int deleteByPrimaryKey(Long id);

    SecurityQuestion selectByPrimaryKey(Long id);

    int insertBatch(@Param("list") List<SecurityQuestion> list);

    List<SecurityQuestion> selectByMemberId(@Param("memberId") Long memberId);

    List<SecurityQuestion> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    Long countByMemberId(@Param("memberId") Long memberId);

}