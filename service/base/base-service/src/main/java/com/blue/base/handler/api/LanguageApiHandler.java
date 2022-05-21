package com.blue.base.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.message.LanguageInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.blue.base.common.message.MessageProcessor.getDefaultLanguage;
import static com.blue.base.common.message.MessageProcessor.listSupportLanguages;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/**
 * language api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class LanguageApiHandler {

    private static final List<LanguageInfo> LANGUAGES = listSupportLanguages();
    private static final LanguageInfo DEFAULT_LANGUAGE = getDefaultLanguage();

    /**
     * select language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return just(LANGUAGES)
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ls, serverRequest), BlueResponse.class)
                );
    }

    /**
     * get default language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getDefault(ServerRequest serverRequest) {
        return just(DEFAULT_LANGUAGE)
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ls, serverRequest), BlueResponse.class)
                );
    }

}
