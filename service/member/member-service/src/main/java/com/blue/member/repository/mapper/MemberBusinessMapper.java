package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberBusiness;

public interface MemberBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MemberBusiness record);

    int insertSelective(MemberBusiness record);

    MemberBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemberBusiness record);

    int updateByPrimaryKey(MemberBusiness record);
}