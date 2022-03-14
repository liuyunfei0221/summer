package com.blue.auth.repository.mapper;

import com.blue.auth.repository.entity.Credential;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * credential dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface CredentialMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Credential record);

    int insertSelective(Credential record);

    Credential selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Credential record);

    int updateByPrimaryKey(Credential record);

    Credential getByCredentialAndType(@Param("credential") String credential, @Param("type") String type);

    Credential getByMemberIdAndType(@Param("memberId") Long memberId, @Param("type") String type);

    List<Credential> selectByMemberId(@Param("memberId") Long memberId);

    List<Credential> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    List<Credential> selectByCredentials(@Param("credentials") List<String> credentials);

    int insertBatch(@Param("list") List<Credential> list);

    int updateAccess(@Param("memberId") Long memberId, @Param("loginTypes") List<String> loginTypes, @Param("access") String access);

}