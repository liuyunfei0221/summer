package com.blue.auth.component.session.impl;

import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.model.SessionInfo;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.CredentialService;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
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
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.auth.CredentialType.ACCOUNT_PWD;
import static com.blue.basic.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.basic.constant.common.BlueHeader.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * account and password session handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "unused"})
public class AccountAndPwdSessionHandler implements SessionHandler {

    private static final Logger LOGGER = getLogger(AccountAndPwdSessionHandler.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final CredentialService credentialService;

    private final AuthService authService;

    public AccountAndPwdSessionHandler(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, CredentialService credentialService, AuthService authService) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.credentialService = credentialService;
        this.authService = authService;
    }

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(DATA_HAS_BEEN_FROZEN);
    };

    private static final Map<String, Object> EXTRA_INFO = new HashMap<>(2);

    static {
        EXTRA_INFO.put(NEW_MEMBER.key, false);
    }

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("AccountAndPwdLoginHandler -> Mono<ServerResponse> session(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (isNull(loginParam))
            throw new BlueException(EMPTY_PARAM);

        String account = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(account) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        return credentialService.getCredentialMonoByCredentialAndType(account, ACCOUNT_PWD.identity)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(INVALID_ACCT_OR_PWD))))
                .flatMap(credential ->
                        just(ofNullable(credential)
                                .filter(c -> VALID.status == c.getStatus())
                                .filter(c -> isNotBlank(c.getAccess()))
                                .filter(c -> matchAccess(access, c.getAccess()))
                                .map(Credential::getMemberId)
                                .orElseThrow(() -> new BlueException(INVALID_ACCT_OR_PWD)))
                ).flatMap(rpcMemberBasicServiceConsumer::getMemberBasicInfoByPrimaryKey)
                .flatMap(mbi -> {
                    MEMBER_STATUS_ASSERTER.accept(mbi);
                    return authService.generateAuthMono(mbi.getId(), ACCOUNT_PWD.identity, loginParam.getDeviceType().intern())
                            .flatMap(ma -> ok().contentType(APPLICATION_JSON)
                                    .header(AUTHORIZATION.name, ma.getAuth())
                                    .header(SECRET.name, ma.getSecKey())
                                    .header(REFRESH.name, ma.getRefresh())
                                    .body(success(new SessionInfo(mbi, EXTRA_INFO), serverRequest)
                                            , BlueResponse.class));
                });
    }

    @Override
    public CredentialType targetType() {
        return ACCOUNT_PWD;
    }

}
