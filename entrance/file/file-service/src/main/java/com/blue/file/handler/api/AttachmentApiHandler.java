package com.blue.file.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.WithdrawInfo;
import com.blue.file.service.inter.AttachmentService;
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
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * attachment api handler
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
     * select attachment by page
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listAttachment(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        attachmentService.selectAttachmentByPageAndMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(vo ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, vo, OK.message), BlueResponse.class));
    }


    /**
     * test encrypt in file project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(WithdrawInfo.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    System.err.println(tuple2.getT2());
                    System.err.println();
                    System.err.println(tuple2.getT1());

                    return Mono.just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, OK.message), BlueResponse.class));
    }


}
