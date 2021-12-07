package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.Credential;
import org.apache.ibatis.annotations.Param;

/**
 * credential mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "UnusedReturnValue"})
public interface CredentialMapper {

    int insert(Credential record);

    int insertSelective(Credential record);

    int updateByPrimaryKey(Credential record);

    int updateByPrimaryKeySelective(Credential record);

    int deleteByPrimaryKey(Long id);

    Credential selectByPrimaryKey(Long id);

    Credential selectCredentialByIdentityAndLoginType(@Param("identity") String identity, @Param("loginType") String loginType);

}