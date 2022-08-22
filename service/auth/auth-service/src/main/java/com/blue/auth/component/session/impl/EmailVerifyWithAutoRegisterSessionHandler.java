package com.blue.auth.component.session.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.model.MemberAuth;
import com.blue.auth.model.SessionInfo;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.AutoRegisterService;
import com.blue.auth.service.inter.CredentialService;
import com.blue.auth.service.inter.RoleService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
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
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.ConstantProcessor.assertSource;
import static com.blue.basic.common.base.SourceGetter.getSource;
import static com.blue.basic.constant.auth.CredentialType.EMAIL_PWD;
import static com.blue.basic.constant.auth.CredentialType.EMAIL_VERIFY_AUTO_REGISTER;
import static com.blue.basic.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.basic.constant.common.BlueHeader.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.member.SourceType.APP;
import static com.blue.basic.constant.verify.VerifyBusinessType.EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER;
import static com.blue.basic.constant.verify.VerifyType.MAIL;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * email and verify with auto register handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "unused"})
public class EmailVerifyWithAutoRegisterSessionHandler implements SessionHandler {

    private static final Logger LOGGER = getLogger(EmailVerifyWithAutoRegisterSessionHandler.class);

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final AutoRegisterService autoRegisterService;

    private final CredentialService credentialService;

    private final RoleService roleService;

    private final AuthService authService;

    public EmailVerifyWithAutoRegisterSessionHandler(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer,
                                                     AutoRegisterService autoRegisterService, CredentialService credentialService,
                                                     RoleService roleService, AuthService authService) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.autoRegisterService = autoRegisterService;
        this.credentialService = credentialService;
        this.roleService = roleService;
        this.authService = authService;
    }

    private static final Function<String, List<CredentialInfo>> CREDENTIALS_GENERATOR = email -> {
        List<CredentialInfo> credentials = new ArrayList<>(5);

        credentials.add(new CredentialInfo(email, EMAIL_VERIFY_AUTO_REGISTER.identity, EMPTY_DATA.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(email, EMAIL_PWD.identity, EMPTY_DATA.value, INVALID.status, "from auto registry"));

        return credentials;
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(DATA_HAS_BEEN_FROZEN);
    };

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("EmailVerifyWithAutoRegisterLoginHandler -> Mono<ServerResponse> session(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (isNull(loginParam))
            throw new BlueException(EMPTY_PARAM);

        String email = loginParam.getData(IDENTITY.key);
        String access = loginParam.getData(ACCESS.key);

        if (isBlank(email) || isBlank(access))
            throw new BlueException(INVALID_ACCT_OR_PWD);

        String source = ofNullable(getSource(serverRequest))
                .filter(BlueChecker::isNotBlank).orElse(APP.identity);
        assertSource(source, false);

        Map<String, Object> extra = new HashMap<>(2, 2.0f);
        return rpcVerifyHandleServiceConsumer.validate(MAIL, EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER, email, access, true)
                .flatMap(validate ->
                        validate ?
                                credentialService.getCredentialMonoByCredentialAndType(email, EMAIL_VERIFY_AUTO_REGISTER.identity)
                                        .flatMap(credential -> {
                                            extra.put(NEW_MEMBER.key, false);
                                            return rpcMemberBasicServiceConsumer.getMemberBasicInfoByPrimaryKey(credential.getMemberId())
                                                    .flatMap(mbi -> {
                                                        MEMBER_STATUS_ASSERTER.accept(mbi);
                                                        return zip(authService.generateAuthMono(mbi.getId(), EMAIL_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern()), just(mbi));
                                                    });
                                        })
                                        .switchIfEmpty(defer(() -> {
                                            extra.put(NEW_MEMBER.key, true);
                                            return just(roleService.getDefaultRole().getId())
                                                    .flatMap(roleId -> just(autoRegisterService.autoRegisterMemberInfo(CREDENTIALS_GENERATOR.apply(email), roleId, source))
                                                            .flatMap(mbi ->
                                                                    zip(authService.generateAuthMono(mbi.getId(), singletonList(roleId), EMAIL_VERIFY_AUTO_REGISTER.identity, loginParam.getDeviceType().intern()), just(mbi))));
                                        }))
                                        .flatMap(tuple2 -> {
                                            MemberAuth ma = tuple2.getT1();
                                            return ok().contentType(APPLICATION_JSON)
                                                    .header(AUTHORIZATION.name, ma.getAuth())
                                                    .header(SECRET.name, ma.getSecKey())
                                                    .header(REFRESH.name, ma.getRefresh())
                                                    .header(RESPONSE_EXTRA.name, GSON.toJson(extra))
                                                    .body(success(new SessionInfo(tuple2.getT2(), extra), serverRequest)
                                                            , BlueResponse.class);
                                        })
                                :
                                error(() -> new BlueException(VERIFY_IS_INVALID))
                );
    }

    @Override
    public CredentialType targetType() {
        return EMAIL_VERIFY_AUTO_REGISTER;
    }

}
