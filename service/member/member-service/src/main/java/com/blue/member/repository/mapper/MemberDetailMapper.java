package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberDetail;

public interface MemberDetailMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MemberDetail record);

    int insertSelective(MemberDetail record);

    MemberDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemberDetail record);

    int updateByPrimaryKey(MemberDetail record);
}