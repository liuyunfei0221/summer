package com.blue.risk.remote.consumer;

import com.blue.base.api.inter.RpcCountryService;
import com.blue.base.api.model.CountryInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc country consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcCountryServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getCountryInfoById", async = true),
                    @Method(name = "selectCountryInfo", async = true),
                    @Method(name = "selectCountryInfoByIds", async = true)
            })
    private RpcCountryService rpcCountryService;

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    public Mono<CountryInfo> getCountryInfoById(Long id) {
        return fromFuture(rpcCountryService.getCountryInfoById(id));
    }

    /**
     * select all countries mono
     *
     * @return
     */
    public Mono<List<CountryInfo>> selectCountryInfo() {
        return fromFuture(rpcCountryService.selectCountryInfo());
    }

    /**
     * select country info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CountryInfo>> selectCountryInfoByIds(List<Long> ids) {
        return fromFuture(rpcCountryService.selectCountryInfoByIds(ids));
    }

}