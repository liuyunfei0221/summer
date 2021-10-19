package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberCondition;
import reactor.core.publisher.Mono;

/**
 * member authority service
 *
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface MemberAuthorityService {

    /**
     * select member's authority info by page and condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest);

}
