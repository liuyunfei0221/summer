package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcCountryService;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.service.inter.CountryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

/**
 * rpc role provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcCountryService.class,
        version = "1.0",
        methods = {
                @Method(name = "getCountryInfoById", async = true),
                @Method(name = "selectCountryInfo", async = true),
                @Method(name = "selectCountryInfoByIds", async = true)
        })
public class RpcCountryServiceProvider implements RpcCountryService {

    private final CountryService countryService;

    public RpcCountryServiceProvider(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CountryInfo> getCountryInfoById(Long id) {
        return just(id)

                .flatMap(countryService::getCountryInfoMonoById)
                .toFuture();
    }

    /**
     * select all countries mono
     *
     * @return
     */
    @Override
    public CompletableFuture<List<CountryInfo>> selectCountryInfo() {
        return just(true)

                .flatMap(v -> countryService.selectCountryInfoMono())
                .toFuture();
    }

    /**
     * select country info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CountryInfo>> selectCountryInfoByIds(List<Long> ids) {
        return just(ids)

                .flatMap(countryService::selectCountryInfoMonoByIds)
                .toFuture();
    }

}
