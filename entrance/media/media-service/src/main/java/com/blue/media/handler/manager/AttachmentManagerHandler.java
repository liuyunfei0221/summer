package com.blue.media.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.service.inter.AttachmentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.media.constant.MediaTypeReference.PAGE_MODEL_FOR_ATTACHMENT_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * attachment manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AttachmentManagerHandler {

    private final AttachmentService attachmentService;

    public AttachmentManagerHandler(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * select attachment by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> listAttachment(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_ATTACHMENT_CONDITION_TYPE)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(attachmentService::selectAttachmentInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
