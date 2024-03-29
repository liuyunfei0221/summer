package com.blue.auth.component.session.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.model.LoginParam;
import com.blue.auth.model.MemberAuth;
import com.blue.auth.model.SessionInfo;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.remote.consumer.RpcWechatServiceConsumer;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.CredentialService;
import com.blue.auth.service.inter.RegisterService;
import com.blue.auth.service.inter.RoleService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.slf4j.Logger;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BasicElementProcessor.assertPhone;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.ConstantProcessor.assertSource;
import static com.blue.basic.common.base.SourceGetter.getSource;
import static com.blue.basic.constant.auth.CredentialType.*;
import static com.blue.basic.constant.auth.ExtraKey.NEW_MEMBER;
import static com.blue.basic.constant.common.BlueHeader.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SyncKeyPrefix.CREDENTIAL_UPDATE_PRE;
import static com.blue.basic.constant.member.SourceType.WE;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * weChat with auto register handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "AlibabaRemoveCommentedCode", "FieldCanBeLocal", "unused"})
public class WechatWithAutoRegisterSessionHandler implements SessionHandler {

    private static final Logger LOGGER = getLogger(WechatWithAutoRegisterSessionHandler.class);

    private final RpcWechatServiceConsumer rpcWechatServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final RegisterService registerService;

    private final CredentialService credentialService;

    private final RoleService roleService;

    private final AuthService authService;

    private final SynchronizedProcessor synchronizedProcessor;

    public WechatWithAutoRegisterSessionHandler(RpcWechatServiceConsumer rpcWechatServiceConsumer, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, RegisterService registerService,
                                                CredentialService credentialService, RoleService roleService, AuthService authService, SynchronizedProcessor synchronizedProcessor) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.rpcWechatServiceConsumer = rpcWechatServiceConsumer;
        this.registerService = registerService;
        this.credentialService = credentialService;
        this.roleService = roleService;
        this.authService = authService;
        this.synchronizedProcessor = synchronizedProcessor;
    }

    private static final UnaryOperator<String> CREDENTIAL_UPDATE_SYNC_KEY_GEN = credential -> {
        if (isNotBlank(credential))
            return CREDENTIAL_UPDATE_PRE.prefix + credential;

        throw new BlueException(BAD_REQUEST);
    };

    private static final Function<String, List<CredentialInfo>> CREDENTIALS_GENERATOR = phone -> {
        List<CredentialInfo> credentials = new ArrayList<>(4);

        credentials.add(new CredentialInfo(phone, PHONE_VERIFY_AUTO_REGISTER.identity, EMPTY_VALUE.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, PHONE_PWD.identity, EMPTY_VALUE.value, INVALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, LOCAL_PHONE_AUTO_REGISTER.identity, EMPTY_VALUE.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, WECHAT_AUTO_REGISTER.identity, EMPTY_VALUE.value, VALID.status, "from auto registry"));
        credentials.add(new CredentialInfo(phone, MINI_PRO_AUTO_REGISTER.identity, EMPTY_VALUE.value, VALID.status, "from auto registry"));

        return credentials;
    };

    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (isInvalidStatus(memberBasicInfo.getStatus()))
            throw new BlueException(DATA_HAS_BEEN_FROZEN);
    };

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("loginParam = {}", loginParam);
        if (isNull(loginParam))
            throw new BlueException(EMPTY_PARAM);

        //TODO verify param

        String phone = EMPTY_VALUE.value;

        assertPhone(phone);

        String source = ofNullable(getSource(serverRequest))
                .filter(BlueChecker::isNotBlank).orElse(WE.identity);
        assertSource(source, false);

        //TODO
        // like Mono<String> phoneMono = rpcWechatServiceConsumer.getInfo(encryptedData, iv, jsCode);
        Map<String, Object> extra = new HashMap<>(2, 2.0f);
        return credentialService.getCredentialByCredentialAndType(phone, WECHAT_AUTO_REGISTER.identity)
                .flatMap(credential -> {
                    extra.put(NEW_MEMBER.key, false);

                    return rpcMemberBasicServiceConsumer.getMemberBasicInfo(credential.getMemberId())
                            .flatMap(mbi -> {
                                MEMBER_STATUS_ASSERTER.accept(mbi);
                                return zip(authService.generateAuth(mbi.getId(), WECHAT_AUTO_REGISTER.identity, loginParam.getDeviceType().intern()), just(mbi));
                            });
                })
                .switchIfEmpty(defer(() -> {
                    extra.put(NEW_MEMBER.key, true);

                    return synchronizedProcessor.handleSupWithSync(CREDENTIAL_UPDATE_SYNC_KEY_GEN.apply(phone), () ->
                            just(roleService.getDefaultRole().getId())
                                    .flatMap(roleId ->
                                            just(registerService.registerMemberBasic(CREDENTIALS_GENERATOR.apply(phone), roleId, source))
                                                    .flatMap(mbi ->
                                                            zip(authService.generateAuth(mbi.getId(), singletonList(roleId), WECHAT_AUTO_REGISTER.identity, loginParam.getDeviceType().intern()), just(mbi))))
                    );
                }))
                .flatMap(tuple2 -> {
                    MemberAuth ma = tuple2.getT1();
                    return ok().contentType(APPLICATION_JSON)
                            .header(AUTHORIZATION.name, ma.getAccess())
                            .header(SECRET.name, ma.getSecKey())
                            .header(REFRESH.name, ma.getRefresh())
                            .header(RESPONSE_EXTRA.name, GSON.toJson(extra))
                            .body(success(new SessionInfo(tuple2.getT2(), extra), serverRequest)
                                    , BlueResponse.class);
                });
    }

    @Override
    public CredentialType targetType() {
        return WECHAT_AUTO_REGISTER;
    }

}
