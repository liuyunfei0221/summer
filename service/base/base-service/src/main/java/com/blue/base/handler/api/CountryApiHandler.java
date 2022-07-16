package com.blue.base.handler.api;

import com.blue.base.service.inter.CountryService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * country api handler
 *
 * @author liuyunfei
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
                                .body(success(l), BlueResponse.class));
    }

}
