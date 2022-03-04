package com.blue.finance.repository.mapper;

import com.blue.finance.repository.entity.Organization;

/**
 * organization dao
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface OrganizationMapper {

    int insert(Organization record);

    int insertSelective(Organization record);

    int updateByPrimaryKey(Organization record);

    int updateByPrimaryKeySelective(Organization record);

    int deleteByPrimaryKey(Long id);

    Organization selectByPrimaryKey(Long id);

}