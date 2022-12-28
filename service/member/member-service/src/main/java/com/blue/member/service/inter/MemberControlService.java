package com.blue.member.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInitParam;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberBasicCondition;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * member control service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MemberControlService {

    /**
     * member register for auto registry or third party session
     *
     * @param memberInitParam
     * @return
     */
    MemberBasicInfo initMemberBasic(MemberInitParam memberInitParam);

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberId
     * @return
     */
    MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId);

    /**
     * select member's authority info by page and condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest);

}
