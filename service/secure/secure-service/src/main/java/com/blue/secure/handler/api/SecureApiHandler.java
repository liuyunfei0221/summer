package com.blue.secure.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.ClientLoginParam;
import com.blue.secure.service.inter.SecureService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_REQUEST_BODY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * 登录接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class SecureApiHandler {

    private final SecureService secureService;

    public SecureApiHandler(SecureService secureService) {
        this.secureService = secureService;
    }

    /**
     * 客户端登录
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> loginByClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientLoginParam.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_REQUEST_BODY.message)))
                .flatMap(secureService::loginByClient)
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .body(generate(OK.code, "登录成功", "登录成功")
                                        , BlueResponse.class));
    }

    /**
     * 更新密钥接口
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateSecret(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.updateSecKeyByAccess(acc)
                                .flatMap(secKey ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(SECRET.name, secKey)
                                                .body(generate(OK.code, "更新成功", "更新成功")
                                                        , BlueResponse.class)));
    }

    /**
     * 获取权限信息
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getAuthority(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.getAuthorityByAccess(acc)
                                .flatMap(authority ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, authority, "查询成功")
                                                        , BlueResponse.class)));
    }

    /**
     * 注销登录接口
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.invalidAuthByAccess(acc)
                                .flatMap(invalid -> ok().contentType(APPLICATION_JSON)
                                        .header(AUTHORIZATION.name, "")
                                        .body(
                                                generate(OK.code, invalid, invalid ? "注销成功" : "认证已注销,无需再次注销")
                                                , BlueResponse.class)));
    }

}
