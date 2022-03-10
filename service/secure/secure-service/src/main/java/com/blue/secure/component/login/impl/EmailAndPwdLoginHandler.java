package com.blue.secure.component.login.impl;

import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.service.inter.CredentialService;
import com.blue.secure.service.inter.MemberService;
import com.blue.secure.service.inter.SecureService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidStatus;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.secure.ExtraKey.NEW_MEMBER;
import static com.blue.base.constant.secure.LoginType.EMAIL_PWD;
import static com.blue.secure.common.AccessEncoder.matchAccess;
import static com.blue.secure.constant.LoginAttribute.ACCESS;
import static com.blue.secure.constant.LoginAttribute.IDENTITY;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * email and password login handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class EmailAndPwdLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(EmailAndPwdLoginHandler.class);

    private final CredentialService credentialService;

    private final MemberService memberService;

    private final SecureService secureService;

    public EmailAndPwdLoginHandler(CredentialService credentialService, MemberService memberService, SecureService secureService) {
        this.credentialService = credentialService;
        this.memberService = memberService;
        this.secureService = secureService;
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
        LOGGER.info("EmailAndPwdLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String email = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(email) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        return credentialService.getCredentialByCredentialAndType(email, EMAIL_PWD.identity)
                .flatMap(credentialOpt ->
                        just(credentialOpt
                                .filter(c -> matchAccess(access, c.getAccess()))
                                .map(Credential::getMemberId)
                                .orElseThrow(() -> new BlueException(INVALID_ACCT_OR_PWD)))
                ).flatMap(memberService::selectMemberBasicInfoMonoById)
                .flatMap(mbi -> {
                    MEMBER_STATUS_ASSERTER.accept(mbi);
                    return secureService.generateAuthMono(mbi.getId(), EMAIL_PWD.identity, loginParam.getDeviceType().intern());
                })
                .flatMap(ma -> ok().contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION.name, ma.getAuth())
                        .header(SECRET.name, ma.getSecKey())
                        .header(EXTRA.name, GSON.toJson(EXTRA_INFO))
                        .body(generate(OK.code, serverRequest)
                                , BlueResponse.class));
    }

    @Override
    public LoginType targetType() {
        return EMAIL_PWD;
    }

}
