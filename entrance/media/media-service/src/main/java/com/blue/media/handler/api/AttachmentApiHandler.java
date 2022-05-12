package com.blue.media.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.api.model.WithdrawInfo;
import com.blue.media.service.inter.AttachmentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * attachment api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AttachmentApiHandler {

    private final AttachmentService attachmentService;

    public AttachmentApiHandler(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * select attachment by page and current member
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listAttachment(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        (Mono<PageModelResponse<AttachmentInfo>>) attachmentService.selectAttachmentInfoByPageAndMemberId(tuple2.getT1(), tuple2.getT2().getId())
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

    /**
     * test encrypt in media project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(WithdrawInfo.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    System.err.println(tuple2.getT2());
                    System.err.println();
                    System.err.println(tuple2.getT1());

                    return just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, serverRequest), BlueResponse.class));
    }

}
