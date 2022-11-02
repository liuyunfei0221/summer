package com.blue.base.handler.manager;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.repository.template.AreaRepository;
import com.blue.base.repository.template.CityRepository;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.blue.base.constant.BaseColumnName.NAME;
import static com.blue.basic.common.base.CommonFunctions.success;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

/**
 * for region test and data move
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "FieldCanBeLocal", "unused", "CommentedOutCode"})
@Component
public class RegionTestHandler {

    private final CountryRepository countryRepository;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final AreaRepository areaRepository;

    private final AreaService areaService;

    private final CityService cityService;

    private final StateService stateService;

    private final CountryService countryService;

    public RegionTestHandler(CountryRepository countryRepository, StateRepository stateRepository,
                             CityRepository cityRepository, AreaRepository areaRepository, AreaService areaService, CityService cityService,
                             StateService stateService, CountryService countryService) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.areaRepository = areaRepository;
        this.areaService = areaService;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
    }


    public Mono<ServerResponse> move(ServerRequest serverRequest) {

//        List<Country> select = countryMapper.select();
//        countryRepository.saveAll(select).subscribe();
//        List<State> select = stateMapper.select();
//        stateRepository.saveAll(select).subscribe();
//        List<City> select = cityMapper.select();
//        cityRepository.saveAll(select).subscribe();
//        List<Area> select = areaMapper.select();
//        areaRepository.saveAll(select).subscribe();
//
//        int count = select.size();
//        System.err.println(count);

        return ok().contentType(APPLICATION_JSON)
                .body(success("OK", serverRequest), BlueResponse.class);
    }

    public Mono<ServerResponse> region1(ServerRequest serverRequest) {
        return zip(
                //管庄
                areaService.getAreaRegionById(1L),
                //北京/朝阳
                cityService.getCityRegionById(19794L),
                //北京/直辖市/省
                stateService.getStateRegionById(2280L),
                //中国
                countryService.getCountryInfoById(45L)
        )
                .flatMap(tuple4 -> {
                    Map<Long, Object> res = new HashMap<>(8, 2.0f);

                    AreaRegion areaRegion = tuple4.getT1();
                    CityRegion cityRegion = tuple4.getT2();
                    StateRegion stateRegion = tuple4.getT3();
                    CountryInfo countryInfo = tuple4.getT4();

                    res.put(countryInfo.getId(), countryInfo);
                    res.put(stateRegion.getStateId(), stateRegion);
                    res.put(cityRegion.getCityId(), cityRegion);
                    res.put(areaRegion.getAreaId(), areaRegion);

                    return just(res);
                }).flatMap(res ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(res, serverRequest), BlueResponse.class)
                );
    }

    public Mono<ServerResponse> region2(ServerRequest serverRequest) {
        return ok().contentType(TEXT_EVENT_STREAM)
                .body(cityRepository.findAll(by(Sort.Order.asc(NAME.name)))
                        .flatMap(city -> cityService.getCityRegionById(city.getId())), CityRegion.class);
    }

}
