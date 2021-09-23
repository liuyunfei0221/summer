package com.blue.member.service.inter;

import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryInfo;
import com.blue.member.repository.entity.MemberBasic;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberBasicService {

    /**
     * 根据手机号获取成员信息
     *
     * @param phone
     * @return
     */
    Mono<Optional<MemberBasic>> getByPhone(String phone);

    /**
     * 根据邮箱获取成员信息
     *
     * @param email
     * @return
     */
    Mono<Optional<MemberBasic>> getByEmail(String email);

    /**
     * 根据主键获取用户信息
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBasic>> getByPrimaryKey(Long id);

    /**
     * 根据主键获取用户信息并校验
     *
     * @param id
     * @return
     */
    Mono<MemberInfo> getMemberInfoByPrimaryKeyWithAssert(Long id);

    /**
     * 注册账户
     *
     * @param memberRegistryInfo
     * @return
     */
    void insert(MemberRegistryInfo memberRegistryInfo);

    /**
     * 查询用户
     *
     * @return
     */
    Mono<List<MemberBasic>> selectMember();

}
