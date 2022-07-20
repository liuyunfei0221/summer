package com.blue.member.service.impl;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.AddressInfo;
import com.blue.member.config.deploy.AddressDeploy;
import com.blue.member.constant.AddressSortAttribute;
import com.blue.member.model.AddressCondition;
import com.blue.member.model.AddressInsertParam;
import com.blue.member.model.AddressUpdateParam;
import com.blue.member.remote.consumer.RpcAreaServiceConsumer;
import com.blue.member.remote.consumer.RpcCityServiceConsumer;
import com.blue.member.repository.entity.Address;
import com.blue.member.repository.template.AddressRepository;
import com.blue.member.service.inter.AddressService;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SyncKeyPrefix.ADDRESS_UPDATE_PRE;
import static com.blue.member.constant.ColumnName.*;
import static com.blue.member.converter.MemberModelConverters.ADDRESSES_2_ADDRESSES_INFO;
import static com.blue.member.converter.MemberModelConverters.ADDRESS_2_ADDRESS_INFO;
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * address service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = getLogger(AddressServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private RpcAreaServiceConsumer rpcAreaServiceConsumer;

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final AddressRepository addressRepository;

    public AddressServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                              RpcAreaServiceConsumer rpcAreaServiceConsumer, RpcCityServiceConsumer rpcCityServiceConsumer, AddressRepository addressRepository, AddressDeploy addressDeploy) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.rpcAreaServiceConsumer = rpcAreaServiceConsumer;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.addressRepository = addressRepository;

        this.MAX_ADDRESS = addressDeploy.getMax();
    }

    private final long MAX_ADDRESS;

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AddressSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private final BiConsumer<Long, Address> ADDRESS_AREA_PACKAGER = (areaId, address) -> {
        if (isInvalidIdentity(areaId) || isNull(address))
            return;

        AreaRegion areaRegion = rpcAreaServiceConsumer.getAreaRegionById(areaId)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture().join();

        ofNullable(areaRegion.getCountry())
                .ifPresent(countryInfo -> {
                    address.setCountryId(countryInfo.getId());
                    address.setCountry(countryInfo.getName());
                });

        ofNullable(areaRegion.getState())
                .ifPresent(stateInfo -> {
                    address.setStateId(stateInfo.getId());
                    address.setState(stateInfo.getName());
                });

        ofNullable(areaRegion.getCity())
                .ifPresent(cityInfo -> {
                    address.setCityId(cityInfo.getId());
                    address.setCity(cityInfo.getName());
                });

        ofNullable(areaRegion.getArea())
                .ifPresent(areaInfo -> {
                    address.setAreaId(areaInfo.getId());
                    address.setArea(areaInfo.getName());
                });
    };

    private final BiConsumer<Long, Address> ADDRESS_CITY_PACKAGER = (cityId, address) -> {
        if (isInvalidIdentity(cityId) || isNull(address))
            return;

        CityRegion cityRegion = rpcCityServiceConsumer.getCityRegionById(cityId)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture().join();

        ofNullable(cityRegion.getCountry())
                .ifPresent(countryInfo -> {
                    address.setCountryId(countryInfo.getId());
                    address.setCountry(countryInfo.getName());
                });

        ofNullable(cityRegion.getState())
                .ifPresent(stateInfo -> {
                    address.setStateId(stateInfo.getId());
                    address.setState(stateInfo.getName());
                });

        ofNullable(cityRegion.getCity())
                .ifPresent(cityInfo -> {
                    address.setCityId(cityInfo.getId());
                    address.setCity(cityInfo.getName());
                });

        address.setAreaId(BLUE_ID.value);
        address.setArea(EMPTY_DATA.value);
    };

    private void packageAddressRegion(Long areaId, Long cityId, Address address) {
        if (isNull(address))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(areaId) && isInvalidIdentity(cityId))
            throw new BlueException(EMPTY_PARAM);

        if (isValidIdentity(areaId)) {
            ADDRESS_AREA_PACKAGER.accept(areaId, address);
        } else {
            ADDRESS_CITY_PACKAGER.accept(cityId, address);
        }
    }

    private final BiFunction<AddressInsertParam, Long, Mono<Address>> ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS = (addressInsertParam, memberId) -> {
        if (isNull(addressInsertParam))
            throw new BlueException(EMPTY_PARAM);
        addressInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        Address address = new Address();

        packageAddressRegion(addressInsertParam.getAreaId(), addressInsertParam.getCityId(), address);

        address.setId(blueIdentityProcessor.generate(Address.class));

        address.setMemberId(memberId);

        address.setContact(addressInsertParam.getContact());
        address.setGender(addressInsertParam.getGender());
        address.setPhone(addressInsertParam.getPhone());
        address.setEmail(addressInsertParam.getEmail());
        address.setDetail(addressInsertParam.getDetail());
        address.setReference(addressInsertParam.getReference());
        address.setExtra(addressInsertParam.getExtra());
        address.setStatus(VALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        address.setCreateTime(stamp);
        address.setUpdateTime(stamp);

        return just(address);
    };

    private final BiFunction<AddressUpdateParam, Address, Mono<Address>> ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS = (addressUpdateParam, address) -> {
        if (isNull(addressUpdateParam) || isNull(address))
            throw new BlueException(EMPTY_PARAM);
        addressUpdateParam.asserts();

        packageAddressRegion(addressUpdateParam.getAreaId(), addressUpdateParam.getCityId(), address);

        ofNullable(addressUpdateParam.getContact())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setContact);
        ofNullable(addressUpdateParam.getGender())
                .ifPresent(gi -> {
                    assertGenderIdentity(gi, true);
                    address.setGender(gi);
                });
        ofNullable(addressUpdateParam.getPhone())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setPhone);
        ofNullable(addressUpdateParam.getEmail())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setEmail);
        ofNullable(addressUpdateParam.getDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setDetail);
        ofNullable(addressUpdateParam.getReference())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setReference);
        ofNullable(addressUpdateParam.getExtra())
                .filter(BlueChecker::isNotBlank).ifPresent(address::setExtra);

        address.setUpdateTime(TIME_STAMP_GETTER.get());

        return just(address);
    };

    private static final Function<Long, String> ADDRESS_UPDATE_SYNC_KEY_GEN = memberId -> ADDRESS_UPDATE_PRE.prefix + memberId;

    private static final Function<AddressCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(AddressSortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };

    private static final Function<AddressCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null){
            query.with(SORTER_CONVERTER.apply(new AddressCondition()));
            return query;
        }

        Address probe = new Address();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(condition.getMemberNameLike()).filter(BlueChecker::isNotBlank).ifPresent(memberNameLike ->
                query.addCriteria(where(MEMBER_NAME.name).regex(compile(PREFIX.element + memberNameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getGender()).ifPresent(probe::setGender);
        ofNullable(condition.getPhoneLike()).filter(BlueChecker::isNotBlank).ifPresent(phoneLike ->
                query.addCriteria(where(PHONE.name).regex(compile(PREFIX.element + phoneLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getEmailLike()).filter(BlueChecker::isNotBlank).ifPresent(emailLike ->
                query.addCriteria(where(EMAIL.name).regex(compile(PREFIX.element + emailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(condition.getStateId()).ifPresent(probe::setStateId);
        ofNullable(condition.getCityId()).ifPresent(probe::setCityId);
        ofNullable(condition.getAreaId()).ifPresent(probe::setAreaId);
        ofNullable(condition.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike ->
                query.addCriteria(where(DETAIL.name).regex(compile(PREFIX.element + detailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getReferenceLike()).filter(BlueChecker::isNotBlank).ifPresent(referenceLike ->
                query.addCriteria(where(REFERENCE.name).regex(compile(PREFIX.element + referenceLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getStatus()).ifPresent(probe::setStatus);

        ofNullable(condition.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(condition.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        ofNullable(condition.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(condition.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(UPDATE_TIME.name).lte(updateTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(condition));

        return query;
    };

    /**
     * insert address
     *
     * @param addressInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<AddressInfo> insertAddress(AddressInsertParam addressInsertParam, Long memberId) {
        LOGGER.info("Mono<AddressInfo> insertAddress(AddressInsertParam addressInsertParam, Long memberId), addressInsertParam = {}, memberId = {}",
                addressInsertParam, memberId);
        if (addressInsertParam == null)
            throw new BlueException(EMPTY_PARAM);
        addressInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () -> {
                    Address probe = new Address();
                    probe.setMemberId(memberId);

                    return addressRepository.count(Example.of(probe))
                            .publishOn(scheduler)
                            .switchIfEmpty(defer(() -> just(0L)))
                            .flatMap(count -> {
                                if (count >= MAX_ADDRESS)
                                    return error(new BlueException(DATA_ALREADY_EXIST));

                                return ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS.apply(addressInsertParam, memberId);
                            })
                            .flatMap(addressRepository::insert)
                            .flatMap(a -> just(ADDRESS_2_ADDRESS_INFO.apply(a)));
                }
        );
    }

    /**
     * update a exist address
     *
     * @param addressUpdateParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<AddressInfo> updateAddress(AddressUpdateParam addressUpdateParam, Long memberId) {
        LOGGER.info("Mono<AddressInfo> updateAddress(AddressUpdateParam addressUpdateParam, Long memberId), addressUpdateParam = {}, memberId = {}", addressUpdateParam, memberId);
        if (isNull(addressUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        addressUpdateParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                addressRepository.findById(addressUpdateParam.getId())
                        .publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(address -> {
                            if (!address.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS.apply(addressUpdateParam, address);
                        })
                        .flatMap(addressRepository::save)
                        .flatMap(a -> just(ADDRESS_2_ADDRESS_INFO.apply(a))));
    }

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    @Override
    public Mono<AddressInfo> deleteAddress(Long id, Long memberId) {
        LOGGER.info("Mono<AddressInfo> deleteAddress(Long id, Long memberId), id = {}, memberId = {}", id, memberId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                addressRepository.findById(id)
                        .publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(address -> {
                            if (!address.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return addressRepository.delete(address)
                                    .then(just(address));
                        })
                        .flatMap(a -> just(ADDRESS_2_ADDRESS_INFO.apply(a))));
    }

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Address> getAddressMono(Long id) {
        LOGGER.info("Mono<Address> getAddressMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return addressRepository.findById(id).publishOn(scheduler);
    }

    /**
     * query address mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<Address>> selectAddressMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<Address>> selectAddressMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        Address probe = new Address();
        probe.setMemberId(memberId);

        return addressRepository.findAll(Example.of(probe)).publishOn(scheduler).collectList();
    }

    /**
     * query address info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<AddressInfo>> selectAddressInfoMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<AddressInfo>> selectAddressInfoMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return selectAddressMonoByMemberId(memberId)
                .flatMap(as -> just(ADDRESSES_2_ADDRESSES_INFO.apply(as)));
    }

    /**
     * query address by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AddressInfo> selectAddressInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<AddressInfo> selectAddressInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getAddressMono)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(a -> {
                    if (isInvalidStatus(a.getStatus()))
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    LOGGER.info("a = {}", a);
                    return just(a);
                }).flatMap(a ->
                        just(ADDRESS_2_ADDRESS_INFO.apply(a))
                );
    }

    /**
     * select address by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AddressInfo>> selectAddressInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<AddressInfo>> selectAddressInfoMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> addressRepository.findAllById(shardIds)
                        .publishOn(scheduler)
                        .map(ADDRESS_2_ADDRESS_INFO))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * select address by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<Address>> selectAddressMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Address>> selectAddressMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Address.class).publishOn(scheduler).collectList();
    }

    /**
     * count address by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countAddressMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countAddressMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Address.class).publishOn(scheduler);
    }

    /**
     * select address info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AddressInfo>> selectAddressInfoPageMonoByPageAndCondition(PageModelRequest<AddressCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<AddressInfo>> selectAddressInfoPageMonoByPageAndCondition(PageModelRequest<AddressCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectAddressMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countAddressMonoByQuery(query)
        ).flatMap(tuple2 -> {
            List<Address> addresses = tuple2.getT1();

            return isNotEmpty(addresses) ?
                    just(new PageModelResponse<>(ADDRESSES_2_ADDRESSES_INFO.apply(addresses), tuple2.getT2()))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}
