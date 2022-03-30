package com.blue.base.handler.manager;

import com.blue.base.api.model.CityRegion;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.mapper.AreaMapper;
import com.blue.base.repository.mapper.CityMapper;
import com.blue.base.repository.mapper.CountryMapper;
import com.blue.base.repository.mapper.StateMapper;
import com.blue.base.repository.template.AreaRepository;
import com.blue.base.repository.template.CityRepository;
import com.blue.base.repository.template.CountryRepository;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

@Component
public class DataMoveHandler {

    private final CountryMapper countryMapper;

    private final CountryRepository countryRepository;

    private final StateMapper stateMapper;

    private final StateRepository stateRepository;

    private final CityMapper cityMapper;

    private final CityRepository cityRepository;

    private final AreaMapper areaMapper;

    private final AreaRepository areaRepository;

    private final CityService cityService;

    private final StateService stateService;

    private final CountryService countryService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DataMoveHandler(CountryMapper countryMapper, CountryRepository countryRepository, StateMapper stateMapper, StateRepository stateRepository,
                           CityMapper cityMapper, CityRepository cityRepository, AreaMapper areaMapper, AreaRepository areaRepository, CityService cityService,
                           StateService stateService, CountryService countryService) {
        this.countryMapper = countryMapper;
        this.countryRepository = countryRepository;
        this.stateMapper = stateMapper;
        this.stateRepository = stateRepository;
        this.cityMapper = cityMapper;
        this.cityRepository = cityRepository;
        this.areaMapper = areaMapper;
        this.areaRepository = areaRepository;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
    }


    public Mono<ServerResponse> move(ServerRequest serverRequest) {

//        List<Country> select = countryMapper.select();
//        countryRepository.saveAll(select).subscribe();
//        List<State> select = stateMapper.select();
//        stateRepository.saveAll(select).subscribe();
        List<City> select = cityMapper.select();
        cityRepository.saveAll(select).subscribe();


        int count = select.size();

        return ok().contentType(APPLICATION_JSON)
                .body(generate(OK.code, count, serverRequest), BlueResponse.class);
    }

    public Mono<ServerResponse> region(ServerRequest serverRequest) {

        return zip(
                cityService.getCityRegionMonoById(19794L),
                stateService.getStateRegionMonoById(2280L),
                countryService.getCountryInfoMonoById(45L)
        )
                .flatMap(tuple3 -> {
                    Map<Long, Object> res = new HashMap<>(8);

                    CountryInfo countryInfo = tuple3.getT3();
                    StateRegion stateRegion = tuple3.getT2();
                    CityRegion cityRegion = tuple3.getT1();

                    res.put(countryInfo.getId(), countryInfo);
                    res.put(stateRegion.getStateId(), stateRegion);
                    res.put(cityRegion.getCityId(), cityRegion);

                    return just(res);
                }).flatMap(res ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, res, serverRequest), BlueResponse.class)
                );
    }

}
