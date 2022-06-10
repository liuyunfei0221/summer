package com.blue.lake.remote.consumer;

import com.blue.base.api.inter.RpcCountryService;
import com.blue.base.api.model.CountryInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc country consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcCountryServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getCountryInfoOptById", async = false),
                    @Method(name = "getCountryInfoById", async = false),
                    @Method(name = "getCountryInfoMonoById", async = true),
                    @Method(name = "selectCountryInfo", async = false),
                    @Method(name = "selectCountryInfoMono", async = true),
                    @Method(name = "selectCountryInfoByIds", async = false),
                    @Method(name = "selectCountryInfoMonoByIds", async = true)
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
    public Optional<CountryInfo> getCountryInfoOptById(Long id) {
        return rpcCountryService.getCountryInfoOptById(id);
    }

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    public CountryInfo getCountryInfoById(Long id) {
        return rpcCountryService.getCountryInfoById(id);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    public Mono<CountryInfo> getCountryInfoMonoById(Long id) {
        return fromFuture(rpcCountryService.getCountryInfoMonoById(id)).subscribeOn(scheduler);
    }

    /**
     * select all countries
     *
     * @return
     */
    public List<CountryInfo> selectCountryInfo() {
        return rpcCountryService.selectCountryInfo();
    }

    /**
     * select all countries mono
     *
     * @return
     */
    public Mono<List<CountryInfo>> selectCountryInfoMono() {
        return fromFuture(rpcCountryService.selectCountryInfoMono()).subscribeOn(scheduler);
    }

    /**
     * select country info by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        return rpcCountryService.selectCountryInfoByIds(ids);
    }

    /**
     * select country info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcCountryService.selectCountryInfoMonoByIds(ids)).subscribeOn(scheduler);
    }

}
