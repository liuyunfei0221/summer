package com.blue.file.handler.api;

import com.blue.base.common.reactive.AccessGetterForReactive;
import com.blue.base.model.base.Access;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.WithdrawInfo;
import com.blue.file.service.inter.AttachmentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_REQUEST_BODY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * 文件控制器
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AttachmentApiHandler {

    private final AttachmentService attachmentService;

    public AttachmentApiHandler(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * 分页查询附件
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listAttachment(ServerRequest serverRequest) {
        Access access = AccessGetterForReactive.getAccess(serverRequest);
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_REQUEST_BODY.message)))
                .flatMap(page ->
                        just(attachmentService.listAttachment(page, access.getId())))
                .flatMap(vo ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, vo, OK.message), BlueResponse.class));
    }


    /**
     * 测试数据加密
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        Access access = AccessGetterForReactive.getAccess(serverRequest);

        return serverRequest.bodyToMono(WithdrawInfo.class)
                .flatMap(withdrawInfo -> {

                    System.err.println(access);
                    System.err.println();
                    System.err.println(withdrawInfo);

                    return Mono.just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, OK.message), BlueResponse.class));

    }


}
