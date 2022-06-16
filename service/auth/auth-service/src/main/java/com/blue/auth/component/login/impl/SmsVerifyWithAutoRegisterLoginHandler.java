package com.blue.auth.component.login.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.AutoRegisterService;
import com.blue.auth.service.inter.CredentialService;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.auth.constant.LoginAttribute.ACCESS;
import static com.blue.auth.constant.LoginAttribute.IDENTITY;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.auth.CredentialType.*;
import static com.blue.base.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.base.constant.common.BlueHeader.*;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.INVALID;
import static com.blue.base.constant.common.Status.VALID;
import static com.blue.base.constant.verify.BusinessType.PHONE_VERIFY_LOGIN_WITH_AUTO_REGISTER;
import static com.blue.base.constant.verify.VerifyType.SMS;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * sms and verify with auto register handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "unused"})
public class SmsVerifyWithAutoRegisterLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(SmsVerifyWithAutoRegisterLoginHandler.class);

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final AutoRegisterService autoRegisterService;

    private final CredentialService credentialService;

    private final RoleService roleService;

    private final AuthService authService;

    public SmsVerifyWithAutoRegisterLoginHandler(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer,
                                                 AutoRegisterService autoRegisterService, CredentialService credentialService, RoleService roleService, AuthService authService) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.autoRegisterService = autoRegisterService;
        this.credentialService = credentialService;
        this.roleService = roleService;
        this.authService = authService;
    }

    private static final Function<String, List<CredentialInfo>> CREDENTIALS_GENERATOR = phone -> {
        List<CredentialInfo> credentials = new ArrayList<>(4);

        credentials.add(new CredentialInfo(phone, PHONE_VERIFY_AUTO_REGISTER.identity, EMPTY_DATA.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, PHONE_PWD.identity, EMPTY_DATA.value, INVALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, LOCAL_PHONE_AUTO_REGISTER.identity, EMPTY_DATA.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, WECHAT_AUTO_REGISTER.identity, EMPTY_DATA.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, MINI_PRO_AUTO_REGISTER.identity, EMPTY_DATA.value, VALID.status, "from auto registry"));

        return credentials;
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(ACCOUNT_HAS_BEEN_FROZEN);
    };

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("SmsVerifyWithAutoRegisterLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (isNull(loginParam))
            throw new BlueException(EMPTY_PARAM);

        String phone = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(phone) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        Map<String, Object> extra = new HashMap<>(2);

        return rpcVerifyHandleServiceConsumer.validate(SMS, PHONE_VERIFY_LOGIN_WITH_AUTO_REGISTER, phone, access, true)
                .flatMap(validate ->
                        validate ?
                                credentialService.getCredentialMonoByCredentialAndType(phone, PHONE_VERIFY_AUTO_REGISTER.identity)
                                        .flatMap(credentialOpt ->
                                                credentialOpt.map(credential -> {
                                                            extra.put(NEW_MEMBER.key, false);
                                                            return rpcMemberBasicServiceConsumer.getMemberBasicInfoMonoByPrimaryKey(credential.getMemberId())
                                                                    .flatMap(mbi -> {
                                                                        MEMBER_STATUS_ASSERTER.accept(mbi);
                                                                        return authService.generateAuthMono(mbi.getId(), PHONE_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern());
                                                                    });
                                                        })
                                                        .orElseGet(() -> {
                                                            extra.put(NEW_MEMBER.key, true);
                                                            return just(roleService.getDefaultRole().getId())
                                                                    .flatMap(roleId -> just(autoRegisterService.autoRegisterMemberInfo(CREDENTIALS_GENERATOR.apply(phone), roleId, PHONE_VERIFY_AUTO_REGISTER.source))
                                                                            .flatMap(mbi -> authService.generateAuthMono(mbi.getId(), singletonList(roleId), PHONE_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern())));
                                                        })
                                        )
                                        .flatMap(ma ->
                                                ok().contentType(APPLICATION_JSON)
                                                        .header(AUTHORIZATION.name, ma.getAuth())
                                                        .header(SECRET.name, ma.getSecKey())
                                                        .header(REFRESH.name, ma.getRefresh())
                                                        .header(EXTRA.name, GSON.toJson(extra))
                                                        .body(generate(OK.code, serverRequest)
                                                                , BlueResponse.class))
                                :
                                error(() -> new BlueException(VERIFY_IS_INVALID))
                );
    }

    @Override
    public CredentialType targetType() {
        return PHONE_VERIFY_AUTO_REGISTER;
    }

}
