package com.blue.member.service.impl;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
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
import static com.blue.basic.common.base.ConstantProcessor.assertGender;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SortType.DESC;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SyncKeyPrefix.ADDRESS_UPDATE_PRE;
import static com.blue.member.constant.AddressColumnName.*;
import static com.blue.member.converter.MemberModelConverters.ADDRESSES_2_ADDRESSES_INFO;
import static com.blue.member.converter.MemberModelConverters.ADDRESS_2_ADDRESS_INFO;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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

    private BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private RpcAreaServiceConsumer rpcAreaServiceConsumer;

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final AddressRepository addressRepository;

    public AddressServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                              RpcAreaServiceConsumer rpcAreaServiceConsumer, RpcCityServiceConsumer rpcCityServiceConsumer, AddressRepository addressRepository, AddressDeploy addressDeploy) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.rpcAreaServiceConsumer = rpcAreaServiceConsumer;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.addressRepository = addressRepository;

        this.MAX_ADDRESS = addressDeploy.getMax();
    }

    private final long MAX_ADDRESS;

    private final BiConsumer<Address, Long> ADDRESS_AREA_PACKAGER = (address, aid) -> {
        if (isNull(address) || isInvalidIdentity(aid))
            return;

        AreaRegion areaRegion = rpcAreaServiceConsumer.getAreaRegionById(aid)
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

    private final BiConsumer<Address, Long> ADDRESS_CITY_PACKAGER = (address, cid) -> {
        if (isNull(address) || isInvalidIdentity(cid))
            return;

        CityRegion cityRegion = rpcCityServiceConsumer.getCityRegionById(cid)
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
        address.setArea(EMPTY_VALUE.value);
    };

    private void packageAddressRegion(Address address, Long aid, Long cid) {
        if (isNull(address))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(aid) && isInvalidIdentity(cid))
            throw new BlueException(EMPTY_PARAM);

        if (isValidIdentity(aid)) {
            ADDRESS_AREA_PACKAGER.accept(address, aid);
        } else {
            ADDRESS_CITY_PACKAGER.accept(address, cid);
        }
    }

    private final BiFunction<AddressInsertParam, Long, Mono<Address>> ADDRESS_INSERT_PARAM_2_ADDRESS = (p, mid) -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);

        Address address = new Address();

        packageAddressRegion(address, p.getAreaId(), p.getCityId());

        address.setId(blueIdentityProcessor.generate(Address.class));

        address.setMemberId(mid);

        address.setContact(p.getContact());
        address.setGender(p.getGender());
        address.setPhone(p.getPhone());
        address.setEmail(p.getEmail());
        address.setDetail(p.getDetail());
        address.setReference(p.getReference());
        address.setExtra(p.getExtra());
        address.setStatus(VALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        address.setCreateTime(stamp);
        address.setUpdateTime(stamp);

        return just(address);
    };

    private final BiFunction<AddressUpdateParam, Address, Mono<Address>> ADDRESS_UPDATE_PARAM_2_ADDRESS = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        packageAddressRegion(t, p.getAreaId(), p.getCityId());

        ofNullable(p.getContact())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setContact);
        ofNullable(p.getGender())
                .ifPresent(gi -> {
                    assertGender(gi, true);
                    t.setGender(gi);
                });
        ofNullable(p.getPhone())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setPhone);
        ofNullable(p.getEmail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setEmail);
        ofNullable(p.getDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setDetail);
        ofNullable(p.getReference())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setReference);
        ofNullable(p.getExtra())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setExtra);

        t.setUpdateTime(TIME_STAMP_GETTER.get());

        return just(t);
    };

    private static final Function<Long, String> ADDRESS_UPDATE_SYNC_KEY_GEN = memberId -> ADDRESS_UPDATE_PRE.prefix + memberId;

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AddressSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<AddressCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(AddressCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(AddressSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(AddressCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(DESC.identity);

        return sortAttribute.equals(AddressSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, AddressSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<AddressCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c)) {
            query.with(SORTER_CONVERTER.apply(new AddressCondition()));
            return query;
        }

        Address probe = new Address();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(c.getMemberNameLike()).filter(BlueChecker::isNotBlank).ifPresent(memberNameLike ->
                query.addCriteria(where(MEMBER_NAME.name).regex(compile(PREFIX.element + memberNameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getGender()).ifPresent(probe::setGender);
        ofNullable(c.getPhoneLike()).filter(BlueChecker::isNotBlank).ifPresent(phoneLike ->
                query.addCriteria(where(PHONE.name).regex(compile(PREFIX.element + phoneLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getEmailLike()).filter(BlueChecker::isNotBlank).ifPresent(emailLike ->
                query.addCriteria(where(EMAIL.name).regex(compile(PREFIX.element + emailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(c.getStateId()).ifPresent(probe::setStateId);
        ofNullable(c.getCityId()).ifPresent(probe::setCityId);
        ofNullable(c.getAreaId()).ifPresent(probe::setAreaId);
        ofNullable(c.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike ->
                query.addCriteria(where(DETAIL.name).regex(compile(PREFIX.element + detailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getReferenceLike()).filter(BlueChecker::isNotBlank).ifPresent(referenceLike ->
                query.addCriteria(where(REFERENCE.name).regex(compile(PREFIX.element + referenceLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(UPDATE_TIME.name).lte(updateTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(c));

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

        return synchronizedProcessor.handleSupWithSync(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () -> {
                    Address probe = new Address();
                    probe.setMemberId(memberId);

                    return addressRepository.count(Example.of(probe))
                            .switchIfEmpty(defer(() -> just(0L)))
                            .flatMap(count ->
                                    count < MAX_ADDRESS ?
                                            ADDRESS_INSERT_PARAM_2_ADDRESS.apply(addressInsertParam, memberId)
                                            :
                                            error(new BlueException(DATA_ALREADY_EXIST))
                            )
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

        return synchronizedProcessor.handleSupWithSync(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                addressRepository.findById(addressUpdateParam.getId())
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(address ->
                                address.getMemberId().equals(memberId) ?
                                        ADDRESS_UPDATE_PARAM_2_ADDRESS.apply(addressUpdateParam, address)
                                        :
                                        error(new BlueException(DATA_NOT_BELONG_TO_YOU))
                        )
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

        return synchronizedProcessor.handleSupWithSync(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                addressRepository.findById(id)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(address ->
                                address.getMemberId().equals(memberId) ?
                                        addressRepository.delete(address)
                                                .then(just(address))
                                        :
                                        error(new BlueException(DATA_NOT_BELONG_TO_YOU))
                        )
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

        return addressRepository.findById(id);
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

        return addressRepository.findAll(Example.of(probe),
                        process(singletonList(new SortElement(AddressSortAttribute.CREATE_TIME.column, DESC.identity))))
                .collectList();
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
    public Mono<AddressInfo> getAddressInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<AddressInfo> getAddressInfoMonoWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getAddressMono)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(a ->
                        isInvalidStatus(a.getStatus()) ?
                                error(() -> new BlueException(INVALID_DATA_STATUS))
                                :
                                just(a)
                ).flatMap(a ->
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

        return reactiveMongoTemplate.find(listQuery, Address.class).collectList();
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
        return reactiveMongoTemplate.count(query, Address.class);
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

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectAddressMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countAddressMonoByQuery(query)
        ).flatMap(tuple2 ->
                just(new PageModelResponse<>(ADDRESSES_2_ADDRESSES_INFO.apply(tuple2.getT1()), tuple2.getT2()))
        );
    }

}
