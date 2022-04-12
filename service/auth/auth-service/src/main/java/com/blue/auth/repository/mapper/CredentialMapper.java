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

    List<Credential> selectByCredentialAndTypes(@Param("credential") String credential, @Param("types") List<String> types);

    Credential getByMemberIdAndType(@Param("memberId") Long memberId, @Param("type") String type);

    List<Credential> selectByMemberIdAndTypes(@Param("memberId") Long memberId, @Param("types") List<String> types);

    List<Credential> selectByMemberId(@Param("memberId") Long memberId);

    List<Credential> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    List<Credential> selectByCredentials(@Param("credentials") List<String> credentials);

    int insertBatch(@Param("list") List<Credential> list);

    int updateAccessByMemberAndTypes(@Param("memberId") Long memberId, @Param("credentialTypes") List<String> credentialTypes, @Param("access") String access);

}