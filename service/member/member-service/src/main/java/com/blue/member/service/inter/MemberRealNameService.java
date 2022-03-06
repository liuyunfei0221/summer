package com.blue.member.service.inter;

import com.blue.member.api.model.MemberRealNameInfo;
import com.blue.member.repository.entity.MemberRealName;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member real name service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberRealNameService {

    /**
     * query member real name by id
     *
     * @param id
     * @return
     */
    Optional<MemberRealName> selectMemberRealNameByPrimaryKey(Long id);

    /**
     * query member real name mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberRealName>> selectMemberRealNameMonoByPrimaryKey(Long id);

    /**
     * query member real name by member id
     *
     * @param memberId
     * @return
     */
    Optional<MemberRealName> selectMemberRealNameByMemberId(Long memberId);

    /**
     * query member real name mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<MemberRealName>> selectMemberRealNameMonoByMemberId(Long memberId);

    /**
     * query member real name by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * query member real name by id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * insert member real name
     *
     * @param memberRealName
     * @return
     */
    MemberRealNameInfo insertMemberRealName(MemberRealName memberRealName);

    /**
     * select member real name by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberRealNameInfo>> selectMemberRealNameInfoMonoByIds(List<Long> ids);

}
