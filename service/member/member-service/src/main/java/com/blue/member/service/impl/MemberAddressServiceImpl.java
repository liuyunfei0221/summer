package com.blue.member.service.impl;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberAddressInfo;
import com.blue.member.config.deploy.AddressDeploy;
import com.blue.member.constant.MemberAddressSortAttribute;
import com.blue.member.model.MemberAddressCondition;
import com.blue.member.model.MemberAddressInsertParam;
import com.blue.member.model.MemberAddressUpdateParam;
import com.blue.member.remote.consumer.RpcAreaServiceConsumer;
import com.blue.member.remote.consumer.RpcCityServiceConsumer;
import com.blue.member.repository.entity.MemberAddress;
import com.blue.member.repository.template.MemberAddressRepository;
import com.blue.member.service.inter.MemberAddressService;
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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueNumericalValue.BLUE_ID;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.SyncKeyPrefix.ADDRESS_UPDATE_PRE;
import static com.blue.member.constant.ColumnName.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_ADDRESSES_2_MEMBER_ADDRESSES_INFO;
import static com.blue.member.converter.MemberModelConverters.MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO;
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * member address service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class MemberAddressServiceImpl implements MemberAddressService {

    private static final Logger LOGGER = getLogger(MemberAddressServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final RpcAreaServiceConsumer rpcAreaServiceConsumer;

    private final RpcCityServiceConsumer rpcCityServiceConsumer;

    private final MemberAddressRepository memberAddressRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberAddressServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor, RpcAreaServiceConsumer rpcAreaServiceConsumer,
                                    RpcCityServiceConsumer rpcCityServiceConsumer, MemberAddressRepository memberAddressRepository, AddressDeploy addressDeploy) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.rpcAreaServiceConsumer = rpcAreaServiceConsumer;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.memberAddressRepository = memberAddressRepository;

        this.MAX_ADDRESS = addressDeploy.getMax();
    }

    private final long MAX_ADDRESS;

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberAddressSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private void packageAddressRegion(Long areaId, Long cityId, MemberAddress memberAddress) {
        if (isNull(memberAddress))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(areaId) && isInvalidIdentity(cityId))
            throw new BlueException(EMPTY_PARAM);

        if (isValidIdentity(areaId)) {
            AreaRegion areaRegion = rpcAreaServiceConsumer.getAreaRegionMonoById(areaId)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                    .toFuture().join();

            ofNullable(areaRegion.getCountry())
                    .ifPresent(countryInfo -> {
                        memberAddress.setCountryId(countryInfo.getId());
                        memberAddress.setCountry(countryInfo.getName());
                    });

            ofNullable(areaRegion.getState())
                    .ifPresent(stateInfo -> {
                        memberAddress.setStateId(stateInfo.getId());
                        memberAddress.setState(stateInfo.getName());
                    });

            ofNullable(areaRegion.getCity())
                    .ifPresent(cityInfo -> {
                        memberAddress.setCityId(cityInfo.getId());
                        memberAddress.setCity(cityInfo.getName());
                    });

            ofNullable(areaRegion.getArea())
                    .ifPresent(areaInfo -> {
                        memberAddress.setAreaId(areaInfo.getId());
                        memberAddress.setArea(areaInfo.getName());
                    });
        } else {
            CityRegion cityRegion = rpcCityServiceConsumer.getCityRegionMonoById(cityId)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                    .toFuture().join();

            ofNullable(cityRegion.getCountry())
                    .ifPresent(countryInfo -> {
                        memberAddress.setCountryId(countryInfo.getId());
                        memberAddress.setCountry(countryInfo.getName());
                    });

            ofNullable(cityRegion.getState())
                    .ifPresent(stateInfo -> {
                        memberAddress.setStateId(stateInfo.getId());
                        memberAddress.setState(stateInfo.getName());
                    });

            ofNullable(cityRegion.getCity())
                    .ifPresent(cityInfo -> {
                        memberAddress.setCityId(cityInfo.getId());
                        memberAddress.setCity(cityInfo.getName());
                    });

            memberAddress.setAreaId(BLUE_ID.value);
            memberAddress.setArea("");
        }
    }

    private final BiConsumer<MemberAddressInsertParam, MemberAddress> ENTITY_COMMON_ATTR_PACKAGE = (memberAddressInsertParam, memberAddress) -> {
        if (isNull(memberAddressInsertParam) || isNull(memberAddress))
            throw new BlueException(EMPTY_PARAM);

        memberAddress.setMemberName(memberAddressInsertParam.getMemberName());
        memberAddress.setGender(memberAddressInsertParam.getGender());
        memberAddress.setPhone(memberAddressInsertParam.getPhone());
        memberAddress.setEmail(memberAddressInsertParam.getEmail());
        memberAddress.setDetail(memberAddressInsertParam.getDetail());
        memberAddress.setReference(memberAddressInsertParam.getReference());
        memberAddress.setExtra(memberAddressInsertParam.getExtra());
    };

    private final BiFunction<MemberAddressInsertParam, Long, MemberAddress> ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS = (memberAddressInsertParam, memberId) -> {
        if (isNull(memberAddressInsertParam))
            throw new BlueException(EMPTY_PARAM);
        memberAddressInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        MemberAddress memberAddress = new MemberAddress();

        packageAddressRegion(memberAddressInsertParam.getAreaId(), memberAddressInsertParam.getCityId(), memberAddress);

        memberAddress.setId(blueIdentityProcessor.generate(MemberAddress.class));

        memberAddress.setMemberId(memberId);

        ENTITY_COMMON_ATTR_PACKAGE.accept(memberAddressInsertParam, memberAddress);

        memberAddress.setStatus(VALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        memberAddress.setCreateTime(stamp);
        memberAddress.setUpdateTime(stamp);

        return memberAddress;
    };

    private final BiFunction<MemberAddressUpdateParam, MemberAddress, MemberAddress> ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS = (memberAddressUpdateParam, memberAddress) -> {
        if (isNull(memberAddressUpdateParam) || isNull(memberAddress))
            throw new BlueException(EMPTY_PARAM);
        memberAddressUpdateParam.asserts();

        packageAddressRegion(memberAddressUpdateParam.getAreaId(), memberAddressUpdateParam.getCityId(), memberAddress);

        ENTITY_COMMON_ATTR_PACKAGE.accept(memberAddressUpdateParam, memberAddress);

        memberAddress.setUpdateTime(TIME_STAMP_GETTER.get());

        return memberAddress;
    };

    private static final Function<Long, String> ADDRESS_UPDATE_SYNC_KEY_GEN = memberId -> ADDRESS_UPDATE_PRE.prefix + memberId;

    private static final Function<MemberAddressCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(MemberAddressSortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };

    private static final Function<MemberAddressCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        MemberAddress probe = new MemberAddress();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(condition.getMemberNameLike()).ifPresent(memberNameLike ->
                query.addCriteria(where(MEMBER_NAME.name).regex(compile(PREFIX.element + memberNameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getGender()).ifPresent(probe::setGender);
        ofNullable(condition.getPhoneLike()).ifPresent(phoneLike ->
                query.addCriteria(where(PHONE.name).regex(compile(PREFIX.element + phoneLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getEmailLike()).ifPresent(emailLike ->
                query.addCriteria(where(EMAIL.name).regex(compile(PREFIX.element + emailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(condition.getStateId()).ifPresent(probe::setStateId);
        ofNullable(condition.getCityId()).ifPresent(probe::setCityId);
        ofNullable(condition.getAreaId()).ifPresent(probe::setAreaId);
        ofNullable(condition.getDetailLike()).ifPresent(detailLike ->
                query.addCriteria(where(DETAIL.name).regex(compile(PREFIX.element + detailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getReferenceLike()).ifPresent(referenceLike ->
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
     * @param memberAddressInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberAddressInfo> insertMemberAddress(MemberAddressInsertParam memberAddressInsertParam, Long memberId) {
        LOGGER.info("Mono<MemberAddressInfo> insertMemberAddress(AddressInsertParam addressInsertParam, Long memberId), addressInsertParam = {}, memberId = {}",
                memberAddressInsertParam, memberId);
        if (memberAddressInsertParam == null)
            throw new BlueException(EMPTY_PARAM);
        memberAddressInsertParam.asserts();

        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () -> {
                    MemberAddress probe = new MemberAddress();
                    probe.setMemberId(memberId);

                    return memberAddressRepository.count(Example.of(probe))
                            .switchIfEmpty(defer(() -> just(0L)))
                            .flatMap(count -> {
                                if (count >= MAX_ADDRESS)
                                    return error(new BlueException(DATA_ALREADY_EXIST));

                                return just(ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS.apply(memberAddressInsertParam, memberId));
                            })
                            .flatMap(memberAddressRepository::insert)
                            .flatMap(ma -> just(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(ma)));
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
    public Mono<MemberAddressInfo> updateMemberAddress(MemberAddressUpdateParam addressUpdateParam, Long memberId) {
        LOGGER.info("Mono<MemberAddressInfo> updateMemberAddress(AddressUpdateParam addressUpdateParam, Long memberId), addressUpdateParam = {}, memberId = {}", addressUpdateParam, memberId);
        if (isNull(addressUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        addressUpdateParam.asserts();

        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                memberAddressRepository.findById(addressUpdateParam.getId())
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(memberAddress -> {
                            if (!memberAddress.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return just(ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS.apply(addressUpdateParam, memberAddress));
                        })
                        .flatMap(memberAddressRepository::save)
                        .flatMap(ma -> just(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(ma)))
        );
    }

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberAddressInfo> deleteMemberAddress(Long id, Long memberId) {
        LOGGER.info("Mono<MemberAddressInfo> deleteMemberAddress(Long id, Long memberId), id = {}, memberId = {}", id, memberId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(ADDRESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                memberAddressRepository.findById(id)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(memberAddress -> {
                            if (!memberAddress.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return memberAddressRepository.delete(memberAddress)
                                    .then(just(memberAddress));
                        })
                        .flatMap(ma -> just(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(ma)))
        );
    }

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberAddress>> getMemberAddressMono(Long id) {
        LOGGER.info("Mono<Optional<MemberAddress>> selectMemberAddressMonoByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return memberAddressRepository.findById(id)
                .map(Optional::ofNullable);
    }

    /**
     * query address mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<MemberAddress>> selectMemberAddressMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<MemberAddress>> selectMemberAddressMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        MemberAddress probe = new MemberAddress();
        probe.setMemberId(memberId);

        return memberAddressRepository.findAll(Example.of(probe)).collectList();
    }

    /**
     * query address info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<MemberAddressInfo>> selectMemberAddressInfoMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<MemberAddressInfo>> selectMemberAddressInfoMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return selectMemberAddressMonoByMemberId(memberId)
                .flatMap(mas -> just(mas.stream().map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO).collect(toList())));
    }

    /**
     * query address by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberAddressInfo> selectMemberAddressInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberAddressInfo> selectMemberAddressInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getMemberAddressMono)
                .flatMap(maOpt ->
                        maOpt.map(Mono::just)
                                .orElseGet(() -> error(() -> new BlueException(DATA_NOT_EXIST)))
                ).flatMap(ma -> {
                    if (isInvalidStatus(ma.getStatus()))
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    LOGGER.info("ma = {}", ma);
                    return just(ma);
                }).flatMap(mb ->
                        just(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(mb))
                );
    }

    /**
     * select address by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberAddressInfo>> selectMemberAddressInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);

        return isValidIdentities(ids) ?
                fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                        .map(shardIds -> memberAddressRepository.findAllById(shardIds)
                                .map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO))
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList)
                :
                just(emptyList());
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
    public Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndCondition(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);

        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, MemberAddress.class).collectList();
    }

    /**
     * count address by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countMemberAddressMonoByCondition(Query query) {
        LOGGER.info("Mono<Long> countMemberAddressMonoByCondition(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, MemberAddress.class);
    }

    /**
     * select address info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberAddressInfo>> selectMemberAddressInfoPageMonoByPageAndCondition(PageModelRequest<MemberAddressCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberAddressInfo>> selectMemberAddressInfoPageMonoByPageAndCondition(PageModelRequest<MemberAddressCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectMemberAddressMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countMemberAddressMonoByCondition(query)
        ).flatMap(tuple2 -> {
            List<MemberAddress> memberAddresses = tuple2.getT1();

            return isNotEmpty(memberAddresses) ?
                    just(new PageModelResponse<>(MEMBER_ADDRESSES_2_MEMBER_ADDRESSES_INFO.apply(memberAddresses), tuple2.getT2()))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}
