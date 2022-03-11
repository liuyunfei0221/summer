package com.blue.auth.service.impl;

import com.blue.auth.service.inter.MemberService;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.auth.remote.consumer.RpcMemberServiceConsumer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.INVALID_ACCT_OR_PWD;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member auth service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOGGER = getLogger(MemberServiceImpl.class);

    private final RpcMemberServiceConsumer rpcMemberServiceConsumer;

    public MemberServiceImpl(RpcMemberServiceConsumer rpcMemberServiceConsumer) {
        this.rpcMemberServiceConsumer = rpcMemberServiceConsumer;
    }

    /**
     * get member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoById(Long id) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(EMPTY_PARAM);

        return rpcMemberServiceConsumer.selectMemberBasicInfoMonoByPrimaryKey(id)
                .onErrorMap(t -> new BlueException(INVALID_ACCT_OR_PWD))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    return just(memberBasicInfo);
                });
    }

    /**
     * get member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhone(String phone) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhoneWithAssertPwd(String phone, String password), phone = {}", phone);
        if (isBlank(phone))
            throw new BlueException(EMPTY_PARAM);

        return rpcMemberServiceConsumer.selectMemberBasicInfoByPhone(phone)
                .onErrorMap(t -> new BlueException(INVALID_ACCT_OR_PWD))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    return just(memberBasicInfo);
                });
    }

    /**
     * get member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoByEmail(String email) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicInfoMonoByEmailWithAssertPwd(String email, String password), email = {}", email);
        if (isBlank(email))
            throw new BlueException(EMPTY_PARAM);

        return rpcMemberServiceConsumer.selectMemberBasicInfoByEmail(email)
                .onErrorMap(t -> new BlueException(INVALID_ACCT_OR_PWD))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    return just(memberBasicInfo);
                });
    }

}
