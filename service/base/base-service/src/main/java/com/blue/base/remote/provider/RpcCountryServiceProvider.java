package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcCountryService;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.service.inter.CountryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc role provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcCountryService.class, version = "1.0", methods = {
        @Method(name = "getCountryInfoOptById", async = false),
        @Method(name = "getCountryInfoById", async = false),
        @Method(name = "getCountryInfoMonoById", async = true),
        @Method(name = "selectCountryInfo", async = false),
        @Method(name = "selectCountryInfoMono", async = true),
        @Method(name = "selectCountryInfoByIds", async = false),
        @Method(name = "selectCountryInfoMonoByIds", async = true)
})
public class RpcCountryServiceProvider implements RpcCountryService {

    private static final Logger LOGGER = getLogger(RpcCountryServiceProvider.class);

    private final CountryService countryService;

    private final Scheduler scheduler;

    public RpcCountryServiceProvider(CountryService countryService, Scheduler scheduler) {
        this.countryService = countryService;
        this.scheduler = scheduler;
    }

    /**
     * get country info by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<CountryInfo> getCountryInfoOptById(Long id) {
        LOGGER.info("Optional<CountryInfo> getCountryInfoOptById(Long id), id = {}", id);
        return countryService.getCountryInfoOptById(id);
    }

    /**
     * get country info by country id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CountryInfo getCountryInfoById(Long id) {
        LOGGER.info("CountryInfo getCountryInfoById(Long id), id = {}", id);
        return countryService.getCountryInfoById(id);
    }

    /**
     * get country info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CountryInfo> getCountryInfoMonoById(Long id) {
        LOGGER.info("CompletableFuture<CountryInfo> getCountryInfoMonoById(Long id), id = {}", id);
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(countryService::getCountryInfoMonoById)
                .toFuture();
    }

    /**
     * select all countries
     *
     * @return
     */
    @Override
    public List<CountryInfo> selectCountryInfo() {
        LOGGER.info("List<CountryInfo> selectCountryInfo()");
        return countryService.selectCountryInfo();
    }

    /**
     * select all countries mono
     *
     * @return
     */
    @Override
    public CompletableFuture<List<CountryInfo>> selectCountryInfoMono() {
        LOGGER.info("CompletableFuture<List<CountryInfo>> selectCountryInfoMono()");
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> countryService.selectCountryInfoMono())
                .toFuture();
    }

    /**
     * select country infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long, CountryInfo> selectCountryInfoByIds(List<Long> ids), ids = {}", ids);
        return countryService.selectCountryInfoByIds(ids);
    }

    /**
     * select country infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<Map<Long, CountryInfo>> selectCountryInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(countryService::selectCountryInfoMonoByIds)
                .toFuture();
    }

}
