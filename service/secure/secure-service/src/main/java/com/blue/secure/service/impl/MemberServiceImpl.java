package com.blue.secure.service.impl;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.ClientLoginParam;
import com.blue.secure.remote.consumer.RpcMemberServiceConsumer;
import com.blue.secure.service.inter.MemberService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.base.common.base.Asserter.isInvalidStatus;
import static com.blue.base.common.base.Asserter.isNull;
import static com.blue.base.constant.base.CommonException.EMPTY_PARAM_EXP;
import static com.blue.secure.constant.SecureCommonException.ACCOUNT_HAS_BEEN_FROZEN_EXP;
import static com.blue.secure.constant.SecureCommonException.INVALID_ACCT_OR_PWD_EXP;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member auth service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOGGER = getLogger(MemberServiceImpl.class);

    private final RpcMemberServiceConsumer rpcMemberServiceConsumer;

    public MemberServiceImpl(RpcMemberServiceConsumer rpcMemberServiceConsumer) {
        this.rpcMemberServiceConsumer = rpcMemberServiceConsumer;
    }

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final BiConsumer<String, MemberBasicInfo> PWD_ASSERTER = (access, mb) -> {
        if (isNull(access) || isNull(mb) || !ENCODER.matches(access, mb.getPassword()))
            throw INVALID_ACCT_OR_PWD_EXP.exp;
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw ACCOUNT_HAS_BEEN_FROZEN_EXP.exp;
    };

    /**
     * get member by phone and check verify
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhoneWithAssertVerify(ClientLoginParam clientLoginParam) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (isNull(clientLoginParam))
            throw EMPTY_PARAM_EXP.exp;

        //TODO check verify
        //TODO check message verify

        return rpcMemberServiceConsumer.selectMemberBasicByPhone(clientLoginParam.getIdentity())
                .onErrorMap(t -> INVALID_ACCT_OR_PWD_EXP.exp)
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), memberBasicInfo = {}", memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

    /**
     * get member by phone and check password
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhoneWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByPhoneWithAssertPwd(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (isNull(clientLoginParam))
            throw EMPTY_PARAM_EXP.exp;

        //TODO check verify

        return rpcMemberServiceConsumer.selectMemberBasicByPhone(clientLoginParam.getIdentity())
                .onErrorMap(t -> INVALID_ACCT_OR_PWD_EXP.exp)
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByPhoneWithAssertPwd(ClientLoginParam clientLoginParam), memberBasicInfo = {}", memberBasicInfo);
                    PWD_ASSERTER.accept(clientLoginParam.getAccess(), memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

    /**
     * get member by email and check password
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberBasicInfoMonoByEmailWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByEmailWithAssertPwd(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (isNull(clientLoginParam))
            throw EMPTY_PARAM_EXP.exp;

        //TODO check verify

        return rpcMemberServiceConsumer.selectMemberBasicByEmail(clientLoginParam.getIdentity())
                .onErrorMap(t -> INVALID_ACCT_OR_PWD_EXP.exp)
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByEmailWithAssertPwd(ClientLoginParam clientLoginParam), memberBasicInfo = {}", memberBasicInfo);
                    PWD_ASSERTER.accept(clientLoginParam.getAccess(), memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

}
