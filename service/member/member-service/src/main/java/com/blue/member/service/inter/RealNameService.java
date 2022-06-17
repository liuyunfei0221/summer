package com.blue.member.service.inter;

import com.blue.member.api.model.MemberRealNameInfo;
import com.blue.member.repository.entity.RealName;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member real name service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RealNameService {

    /**
     * query member real name by id
     *
     * @param id
     * @return
     */
    Optional<RealName> getMemberRealName(Long id);

    /**
     * query member real name mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<RealName>> getMemberRealNameMono(Long id);

    /**
     * query member real name by member id
     *
     * @param memberId
     * @return
     */
    Optional<RealName> getMemberRealNameByMemberId(Long memberId);

    /**
     * query member real name mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<RealName>> getMemberRealNameMonoByMemberId(Long memberId);

    /**
     * query member real name by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberRealNameInfo> getMemberRealNameInfoMonoWithAssert(Long id);

    /**
     * query member real name by id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberRealNameInfo> getMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * insert member real name
     *
     * @param realName
     * @return
     */
    MemberRealNameInfo insertMemberRealName(RealName realName);

    /**
     * select member real name by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberRealNameInfo>> selectMemberRealNameInfoMonoByIds(List<Long> ids);

}
