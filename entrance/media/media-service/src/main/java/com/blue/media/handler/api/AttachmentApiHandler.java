package com.blue.media.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.service.inter.AttachmentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.media.constant.MediaTypeReference.SCROLL_MODEL_FOR_ATTACHMENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * attachment api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class AttachmentApiHandler {

    private final AttachmentService attachmentService;

    public AttachmentApiHandler(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * select attachment by scroll and current member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scroll(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(SCROLL_MODEL_FOR_ATTACHMENT_TYPE)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> attachmentService.selectShineInfoScrollMonoByScrollAndCursorBaseOnMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(smr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(smr, serverRequest), BlueResponse.class));
    }

}
