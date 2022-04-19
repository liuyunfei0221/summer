package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberBasicCondition;
import reactor.core.publisher.Mono;

/**
 * member authority service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MemberAuthorityService {

    /**
     * select member's authority info by page and condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest);

}
