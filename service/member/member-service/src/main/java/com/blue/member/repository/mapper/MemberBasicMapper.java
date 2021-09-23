package com.blue.member.repository.mapper;

import com.blue.member.model.MemberCondition;
import com.blue.member.repository.entity.MemberBasic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成员持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberBasicMapper {

    /**
     * 新增成员
     *
     * @param record
     * @return
     */
    int insert(MemberBasic record);

    /**
     * 新增成员
     *
     * @param record
     * @return
     */
    int insertSelective(MemberBasic record);

    /**
     * 更新成员
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(MemberBasic record);

    /**
     * 根据主键查询成员
     *
     * @param id
     * @return
     */
    MemberBasic selectByPrimaryKey(Long id);

    /**
     * 根据成员手机号查询成员
     *
     * @param phone
     * @return
     */
    MemberBasic selectByPhone(@Param("phone") String phone);

    /**
     * 根据成员邮箱地址查询成员
     *
     * @param email
     * @return
     */
    MemberBasic selectByEmail(@Param("email") String email);

    /**
     * 根据成员昵称查询成员
     *
     * @param name
     * @return
     */
    MemberBasic selectByName(@Param("name") String name);

    /**
     * 查询成员
     *
     * @param memberCondition
     * @return
     */
    List<MemberBasic> selectByCondition(@Param("memberCondition") MemberCondition memberCondition);

}