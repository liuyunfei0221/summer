package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberRealNameInfo;
import com.blue.member.model.MemberRealNameCondition;
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
    List<MemberRealName> selectMemberRealNameByMemberId(Long memberId);

    /**
     * query member real name mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberRealName>> selectMemberRealNameMonoByMemberId(Long memberId);

    /**
     * query member real name by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByPrimaryKeyWithAssert(Long id);

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
    Mono<List<MemberRealName>> selectMemberRealNameMonoByIds(List<Long> ids);

    /**
     * select member real name by page and condition
     *
     * @param limit
     * @param rows
     * @param memberRealNameCondition
     * @return
     */
    Mono<List<MemberRealName>> selectMemberRealNameMonoByLimitAndCondition(Long limit, Long rows, MemberRealNameCondition memberRealNameCondition);

    /**
     * count member real name by condition
     *
     * @param memberRealNameCondition
     * @return
     */
    Mono<Long> countMemberRealNameMonoByCondition(MemberRealNameCondition memberRealNameCondition);

    /**
     * select member real name info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberRealNameInfo>> selectMemberRealNameInfoPageMonoByPageAndCondition(PageModelRequest<MemberRealNameCondition> pageModelRequest);

}
