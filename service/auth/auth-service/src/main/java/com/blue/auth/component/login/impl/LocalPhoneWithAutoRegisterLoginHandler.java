package com.blue.auth.component.login.impl;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.service.inter.*;
import com.blue.base.constant.auth.LoginType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcLocalPhoneServiceConsumer;
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

import static com.blue.base.common.base.BlueChecker.isInvalidStatus;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.base.constant.auth.LoginType.LOCAL_PHONE_AUTO_REGISTER;
import static java.util.Collections.singletonList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * weChat with auto register handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "AlibabaRemoveCommentedCode", "FieldCanBeLocal"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class LocalPhoneWithAutoRegisterLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(LocalPhoneWithAutoRegisterLoginHandler.class);

    private final RpcLocalPhoneServiceConsumer rpcLocalPhoneServiceConsumer;

    private final AutoRegisterService autoRegisterService;

    private final CredentialService credentialService;

    private final RoleService roleService;

    private final MemberService memberService;

    private final AuthService authService;

    public LocalPhoneWithAutoRegisterLoginHandler(RpcLocalPhoneServiceConsumer rpcLocalPhoneServiceConsumer, AutoRegisterService autoRegisterService,
                                                  CredentialService credentialService, RoleService roleService, MemberService memberService, AuthService authService) {
        this.rpcLocalPhoneServiceConsumer = rpcLocalPhoneServiceConsumer;
        this.autoRegisterService = autoRegisterService;
        this.credentialService = credentialService;
        this.roleService = roleService;
        this.memberService = memberService;
        this.authService = authService;
    }

    private static final Function<String, List<CredentialInfo>> CREDENTIALS_GENERATOR = phone -> {
        CredentialInfo credential = new CredentialInfo();

        credential.setCredential(phone);
        credential.setType(LOCAL_PHONE_AUTO_REGISTER.identity);

        return singletonList(credential);
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(ACCOUNT_HAS_BEEN_FROZEN);
    };

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("WechatWithAutoRegisterLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        //TODO verify param

        String phone = "";

        Map<String, Object> extra = new HashMap<>(2);

        //TODO
        // like Mono<String> phoneMono = rpcLocalPhoneServiceConsumer.getInfo(encryptedData, iv, jsCode);
        return credentialService.getCredentialByCredentialAndType(phone, LOCAL_PHONE_AUTO_REGISTER.identity)
                .flatMap(credentialOpt ->
                        credentialOpt.map(credential -> {
                                    extra.put(NEW_MEMBER.key, false);
                                    return memberService.selectMemberBasicInfoMonoById(credential.getMemberId())
                                            .flatMap(mbi -> {
                                                MEMBER_STATUS_ASSERTER.accept(mbi);
                                                return authService.generateAuthMono(mbi.getId(), LOCAL_PHONE_AUTO_REGISTER.identity, loginParam.getDeviceType().intern());
                                            });
                                })
                                .orElseGet(() -> {
                                    extra.put(NEW_MEMBER.key, true);
                                    return just(roleService.getDefaultRole().getId())
                                            .flatMap(roleId -> just(autoRegisterService.autoRegisterMemberInfo(CREDENTIALS_GENERATOR.apply(phone), roleId))
                                                    .flatMap(mbi -> authService.generateAuthMono(mbi.getId(), roleId, LOCAL_PHONE_AUTO_REGISTER.identity, loginParam.getDeviceType().intern())));
                                })
                )
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .header(EXTRA.name, GSON.toJson(extra))
                                .body(generate(OK.code, serverRequest)
                                        , BlueResponse.class));

    }

    @Override
    public LoginType targetType() {
        return LOCAL_PHONE_AUTO_REGISTER;
    }

}
