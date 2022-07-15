package com.blue.media.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.service.inter.AttachmentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * attachment api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "ReactiveStreamsUnusedPublisher"})
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
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> attachmentService.selectAttachmentDetailInfoByPageAndMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
