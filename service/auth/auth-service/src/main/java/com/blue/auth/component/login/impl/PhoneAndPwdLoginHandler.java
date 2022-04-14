package com.blue.auth.component.login.impl;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcMemberServiceConsumer;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.CredentialService;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.blue.auth.common.AccessEncoder.matchAccess;
import static com.blue.auth.constant.LoginAttribute.ACCESS;
import static com.blue.auth.constant.LoginAttribute.IDENTITY;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.base.constant.auth.CredentialType.EMAIL_PWD;
import static com.blue.base.constant.auth.CredentialType.PHONE_PWD;
import static com.blue.base.constant.base.BlueHeader.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * phone and password login handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public class PhoneAndPwdLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(PhoneAndPwdLoginHandler.class);

    private final RpcMemberServiceConsumer rpcMemberServiceConsumer;

    private final CredentialService credentialService;

    private final AuthService authService;

    public PhoneAndPwdLoginHandler(RpcMemberServiceConsumer rpcMemberServiceConsumer, CredentialService credentialService, AuthService authService) {
        this.rpcMemberServiceConsumer = rpcMemberServiceConsumer;
        this.credentialService = credentialService;
        this.authService = authService;
    }

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(ACCOUNT_HAS_BEEN_FROZEN);
    };

    private static final Map<String, Object> EXTRA_INFO = new HashMap<>(2);

    static {
        EXTRA_INFO.put(NEW_MEMBER.key, false);
    }

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("PhoneAndPwdLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String phone = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(phone) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        return credentialService.getCredentialMonoByCredentialAndType(phone, PHONE_PWD.identity)
                .flatMap(credentialOpt ->
                        just(credentialOpt
                                .filter(c -> VALID.status == c.getStatus())
                                .filter(c -> isNotBlank(c.getAccess()))
                                .filter(c -> matchAccess(access, c.getAccess()))
                                .map(Credential::getMemberId)
                                .orElseThrow(() -> new BlueException(INVALID_ACCT_OR_PWD)))
                ).flatMap(rpcMemberServiceConsumer::selectMemberBasicInfoMonoByPrimaryKey)
                .flatMap(mbi -> {
                    MEMBER_STATUS_ASSERTER.accept(mbi);
                    return authService.generateAuthMono(mbi.getId(), EMAIL_PWD.identity, loginParam.getDeviceType().intern());
                })
                .flatMap(ma -> ok().contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION.name, ma.getAuth())
                        .header(SECRET.name, ma.getSecKey())
                        .header(REFRESH.name, ma.getRefresh())
                        .header(EXTRA.name, GSON.toJson(EXTRA_INFO))
                        .body(generate(OK.code, serverRequest)
                                , BlueResponse.class));
    }

    @Override
    public CredentialType targetType() {
        return PHONE_PWD;
    }

}
