package com.blue.base.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.service.inter.CountryService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * country api handler
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public class CountryApiHandler {

    private final CountryService countryService;

    public CountryApiHandler(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * select countries
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return countryService.selectCountryInfoMono()
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, l, serverRequest), BlueResponse.class));
    }

}
