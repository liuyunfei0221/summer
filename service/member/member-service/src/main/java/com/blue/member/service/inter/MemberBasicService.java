package com.blue.member.service.inter;

import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.repository.entity.MemberBasic;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member basic service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberBasicService {

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    Mono<Optional<MemberBasic>> getByPhone(String phone);

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    Mono<Optional<MemberBasic>> getByEmail(String email);

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBasic>> getByPrimaryKey(Long id);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberInfo> getMemberInfoByPrimaryKeyWithAssert(Long id);

    /**
     * member registry
     *
     * @param memberRegistryParam
     * @return
     */
    void insert(MemberRegistryParam memberRegistryParam);

    /**
     * select member
     *
     * @return
     */
    Mono<List<MemberBasic>> selectMember();

}
