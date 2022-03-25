package com.blue.member.remote.consumer;

import com.blue.base.api.inter.RpcCountryService;
import com.blue.base.api.model.CountryInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcCountryServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcVerifyServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-verify"}, methods = {
            @Method(name = "getCountryInfoOptById", async = false),
            @Method(name = "getCountryInfoById", async = false),
            @Method(name = "getCountryInfoMonoById", async = true),
            @Method(name = "selectCountryInfo", async = false),
            @Method(name = "selectCountryInfoMono", async = true),
            @Method(name = "selectCountryInfoByIds", async = false),
            @Method(name = "validate", async = true)
    })
    private RpcCountryService rpcCountryService;

    private final Scheduler scheduler;

    public RpcCountryServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get country info by country id
     *
     * @param id
     * @return
     */
    Optional<CountryInfo> getCountryInfoOptById(Long id) {
        return rpcCountryService.getCountryInfoOptById(id);
    }

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    CountryInfo getCountryInfoById(Long id) {
        return rpcCountryService.getCountryInfoById(id);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    Mono<CountryInfo> getCountryInfoMonoById(Long id) {
        return fromFuture(rpcCountryService.getCountryInfoMonoById(id)).publishOn(scheduler);
    }

    /**
     * select all countries
     *
     * @return
     */
    List<CountryInfo> selectCountryInfo() {
        return rpcCountryService.selectCountryInfo();
    }

    /**
     * select all countries mono
     *
     * @return
     */
    Mono<List<CountryInfo>> selectCountryInfoMono() {
        return fromFuture(rpcCountryService.selectCountryInfoMono()).publishOn(scheduler);
    }

    /**
     * select country infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        return rpcCountryService.selectCountryInfoByIds(ids);
    }

    /**
     * select country infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcCountryService.selectCountryInfoMonoByIds(ids)).publishOn(scheduler);
    }

}
