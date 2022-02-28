package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Credential;
import org.apache.ibatis.annotations.Param;

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

}