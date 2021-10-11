package com.blue.secure.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.ClientLoginParam;
import com.blue.secure.remote.consumer.RpcMemberServiceConsumer;
import com.blue.secure.service.inter.MemberAuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.*;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member auth service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private static final Logger LOGGER = getLogger(MemberAuthServiceImpl.class);

    private final RpcMemberServiceConsumer rpcMemberServiceConsumer;

    public MemberAuthServiceImpl(RpcMemberServiceConsumer rpcMemberServiceConsumer) {
        this.rpcMemberServiceConsumer = rpcMemberServiceConsumer;
    }

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final BiConsumer<String, MemberBasicInfo> PWD_ASSERTER = (access, mb) -> {
        if (access == null || mb == null || !ENCODER.matches(access, mb.getPassword()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_ACCT_OR_PWD.message);
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (Status.VALID.status != memberBasicInfo.getStatus())
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, ACCOUNT_HAS_BEEN_FROZEN.message);
    };

    /**
     * get member by phone and check verify
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        //TODO check verify
        //TODO check message verify

        return rpcMemberServiceConsumer.getMemberBasicByPhone(clientLoginParam.getIdentity())
                .onErrorMap(t -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_ACCT_OR_PWD.message))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
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
    public Mono<MemberBasicInfo> getMemberByPhoneWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        //TODO check verify

        return rpcMemberServiceConsumer.getMemberBasicByPhone(clientLoginParam.getIdentity())
                .onErrorMap(t -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_ACCT_OR_PWD.message))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
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
    public Mono<MemberBasicInfo> getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        //TODO check verify

        return rpcMemberServiceConsumer.getMemberBasicByEmail(clientLoginParam.getIdentity())
                .onErrorMap(t -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_ACCT_OR_PWD.message))
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    PWD_ASSERTER.accept(clientLoginParam.getAccess(), memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

}
