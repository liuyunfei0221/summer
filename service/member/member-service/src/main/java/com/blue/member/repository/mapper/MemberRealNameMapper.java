package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberRealName;

public interface MemberRealNameMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MemberRealName record);

    int insertSelective(MemberRealName record);

    MemberRealName selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemberRealName record);

    int updateByPrimaryKey(MemberRealName record);
}