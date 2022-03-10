package com.blue.secure.component.login.impl;

import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.CredentialInfo;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import com.blue.secure.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.secure.service.inter.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidStatus;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.secure.ExtraKey.NEW_MEMBER;
import static com.blue.base.constant.secure.LoginType.EMAIL_VERIFY_AUTO_REGISTER;
import static com.blue.base.constant.verify.BusinessType.EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static com.blue.secure.constant.LoginAttribute.ACCESS;
import static com.blue.secure.constant.LoginAttribute.IDENTITY;
import static java.util.Collections.singletonList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * email and verify with auto register handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class EmailVerifyWithAutoRegisterLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(EmailVerifyWithAutoRegisterLoginHandler.class);

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final AutoRegisterService autoRegisterService;

    private final CredentialService credentialService;

    private final RoleService roleService;

    private final MemberService memberService;

    private final SecureService secureService;

    public EmailVerifyWithAutoRegisterLoginHandler(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer,
                                                   AutoRegisterService autoRegisterService, CredentialService credentialService,
                                                   RoleService roleService, MemberService memberService, SecureService secureService) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.autoRegisterService = autoRegisterService;
        this.credentialService = credentialService;
        this.roleService = roleService;
        this.memberService = memberService;
        this.secureService = secureService;
    }

    private static final Function<String, List<CredentialInfo>> CREDENTIALS_GENERATOR = email -> {
        CredentialInfo credential = new CredentialInfo();

        credential.setCredential(email);
        credential.setType(EMAIL_VERIFY_AUTO_REGISTER.identity);

        return singletonList(credential);
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(ACCOUNT_HAS_BEEN_FROZEN);
    };

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("EmailVerifyWithAutoRegisterLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String email = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(email) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        Map<String, Object> extra = new HashMap<>(2);

        return rpcVerifyHandleServiceConsumer.validate(MAIL, EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER, email, access, true)
                .flatMap(validate ->
                        validate ?
                                credentialService.getCredentialByCredentialAndType(email, EMAIL_VERIFY_AUTO_REGISTER.identity)
                                        .flatMap(credentialOpt ->
                                                credentialOpt.map(credential -> {
                                                            extra.put(NEW_MEMBER.key, false);
                                                            return memberService.selectMemberBasicInfoMonoById(credential.getMemberId())
                                                                    .flatMap(mbi -> {
                                                                        MEMBER_STATUS_ASSERTER.accept(mbi);
                                                                        return secureService.generateAuthMono(mbi.getId(), EMAIL_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern());
                                                                    });
                                                        })
                                                        .orElseGet(() -> {
                                                            extra.put(NEW_MEMBER.key, true);
                                                            return just(roleService.getDefaultRole().getId())
                                                                    .flatMap(roleId -> just(autoRegisterService.autoRegisterMemberInfo(CREDENTIALS_GENERATOR.apply(email), roleId))
                                                                            .flatMap(mbi -> secureService.generateAuthMono(mbi.getId(), roleId, EMAIL_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern())));
                                                        })
                                        )
                                        .flatMap(ma ->
                                                ok().contentType(APPLICATION_JSON)
                                                        .header(AUTHORIZATION.name, ma.getAuth())
                                                        .header(SECRET.name, ma.getSecKey())
                                                        .header(EXTRA.name, GSON.toJson(extra))
                                                        .body(generate(OK.code, serverRequest)
                                                                , BlueResponse.class))
                                :
                                error(() -> new BlueException(VERIFY_IS_INVALID))
                );
    }

    @Override
    public LoginType targetType() {
        return EMAIL_VERIFY_AUTO_REGISTER;
    }

}
