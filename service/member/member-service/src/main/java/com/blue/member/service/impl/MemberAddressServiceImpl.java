package com.blue.member.service.impl;

import com.blue.base.api.model.AreaRegion;
import com.blue.base.api.model.CityRegion;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberAddressInfo;
import com.blue.member.config.deploy.AddressDeploy;
import com.blue.member.constant.MemberAddressSortAttribute;
import com.blue.member.model.MemberAddressInsertParam;
import com.blue.member.model.MemberAddressUpdateParam;
import com.blue.member.model.MemberAddressCondition;
import com.blue.member.remote.consumer.RpcAreaServiceConsumer;
import com.blue.member.remote.consumer.RpcCityServiceConsumer;
import com.blue.member.repository.entity.MemberAddress;
import com.blue.member.repository.mapper.MemberAddressMapper;
import com.blue.member.service.inter.MemberAddressService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.getSortTypeByIdentity;
import static com.blue.base.constant.base.BlueNumericalValue.BLUE_ID;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.member.converter.MemberModelConverters.MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RpcAreaServiceConsumer rpcAreaServiceConsumer;

    private final RpcCityServiceConsumer rpcCityServiceConsumer;

    private MemberAddressMapper memberAddressMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberAddressServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RpcAreaServiceConsumer rpcAreaServiceConsumer, RpcCityServiceConsumer rpcCityServiceConsumer,
                                    MemberAddressMapper memberAddressMapper, AddressDeploy addressDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rpcAreaServiceConsumer = rpcAreaServiceConsumer;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.memberAddressMapper = memberAddressMapper;

        this.maxAddress = addressDeploy.getMax();
    }

    private long maxAddress;

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberAddressSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberAddressCondition> MEMBER_ADDRESS_CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new MemberAddressCondition();

        condition.setSortAttribute(
                ofNullable(condition.getSortAttribute())
                        .filter(BlueChecker::isNotBlank)
                        .map(SORT_ATTRIBUTE_MAPPING::get)
                        .filter(BlueChecker::isNotBlank)
                        .orElseThrow(() -> new BlueException(INVALID_PARAM)));

        condition.setSortType(getSortTypeByIdentity(condition.getSortType()).identity);

        return condition;
    };

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

    public final BiFunction<MemberAddressInsertParam, Long, MemberAddress> ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS = (memberAddressInsertParam, memberId) -> {
        if (memberAddressInsertParam == null)
            throw new BlueException(EMPTY_PARAM);
        memberAddressInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        if (memberAddressMapper.countByMemberId(memberId) >= maxAddress)
            throw new BlueException(DATA_ALREADY_EXIST);

        MemberAddress memberAddress = new MemberAddress();

        packageAddressRegion(memberAddressInsertParam.getAreaId(), memberAddressInsertParam.getCityId(), memberAddress);

        Long stamp = TIME_STAMP_GETTER.get();

        memberAddress.setMemberId(memberId);
        memberAddress.setMemberName(memberAddressInsertParam.getMemberName());
        memberAddress.setGender(memberAddressInsertParam.getGender());
        memberAddress.setPhone(memberAddressInsertParam.getPhone());
        memberAddress.setEmail(memberAddressInsertParam.getEmail());
        memberAddress.setAddress(memberAddressInsertParam.getAddress());
        memberAddress.setReference(memberAddressInsertParam.getReference());
        memberAddress.setExtra(memberAddressInsertParam.getExtra());
        memberAddress.setStatus(VALID.status);
        memberAddress.setCreateTime(stamp);
        memberAddress.setUpdateTime(stamp);

        return memberAddress;
    };

    public final BiFunction<MemberAddressUpdateParam, Long, MemberAddress> ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS = (addressUpdateParam, memberId) -> {
        if (addressUpdateParam == null)
            throw new BlueException(EMPTY_PARAM);
        addressUpdateParam.asserts();

        Long id = addressUpdateParam.getId();

        MemberAddress memberAddress = memberAddressMapper.selectByPrimaryKey(id);
        if (isNull(memberAddress))
            throw new BlueException(DATA_NOT_EXIST);
        if (!memberAddress.getMemberId().equals(memberId))
            throw new BlueException(DATA_NOT_BELONG_TO_YOU);

        packageAddressRegion(addressUpdateParam.getAreaId(), addressUpdateParam.getCityId(), memberAddress);

        Long stamp = TIME_STAMP_GETTER.get();

        memberAddress.setMemberName(addressUpdateParam.getMemberName());
        memberAddress.setGender(addressUpdateParam.getGender());
        memberAddress.setPhone(addressUpdateParam.getPhone());
        memberAddress.setEmail(addressUpdateParam.getEmail());
        memberAddress.setAddress(addressUpdateParam.getAddress());
        memberAddress.setReference(addressUpdateParam.getReference());
        memberAddress.setExtra(addressUpdateParam.getExtra());

        memberAddress.setUpdateTime(stamp);

        return memberAddress;
    };


    /**
     * insert address
     *
     * @param memberAddressInsertParam
     * @param memberId
     * @return
     */
    @Override
    public MemberAddressInfo insertMemberAddress(MemberAddressInsertParam memberAddressInsertParam, Long memberId) {
        LOGGER.info("MemberAddressInfo insertMemberAddress(AddressInsertParam addressInsertParam, Long memberId), addressInsertParam = {}, memberId = {}",
                memberAddressInsertParam, memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        MemberAddress memberAddress = ADDRESS_INSERT_PARAM_2_MEMBER_ADDRESS.apply(memberAddressInsertParam, memberId);
        memberAddress.setId(blueIdentityProcessor.generate(MemberAddress.class));

        memberAddressMapper.insert(memberAddress);

        return MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(memberAddress);
    }

    /**
     * update a exist address
     *
     * @param addressUpdateParam
     * @param memberId
     * @return
     */
    @Override
    public MemberAddressInfo updateMemberAddress(MemberAddressUpdateParam addressUpdateParam, Long memberId) {
        LOGGER.info("MemberAddressInfo updateMemberAddress(AddressUpdateParam addressUpdateParam, Long memberId), addressUpdateParam = {}, memberId = {}", addressUpdateParam, memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        MemberAddress memberAddress = ADDRESS_UPDATE_PARAM_2_MEMBER_ADDRESS.apply(addressUpdateParam, memberId);

        memberAddressMapper.updateByPrimaryKeySelective(memberAddress);

        return MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(memberAddress);
    }

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    @Override
    public MemberAddressInfo deleteMemberAddress(Long id, Long memberId) {
        LOGGER.info("MemberAddressInfo deleteMemberAddress(Long id, Long memberId), id = {}, memberId = {}", id, memberId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        MemberAddress memberAddress = memberAddressMapper.selectByPrimaryKey(id);
        if (isNull(memberAddress))
            throw new BlueException(DATA_NOT_EXIST);
        if (!memberAddress.getMemberId().equals(memberId))
            throw new BlueException(DATA_NOT_BELONG_TO_YOU);

        memberAddressMapper.deleteByPrimaryKey(id);

        return MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO.apply(memberAddress);
    }

    /**
     * query address by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberAddress> getMemberAddress(Long id) {
        LOGGER.info("Optional<MemberAddress> selectMemberAddressByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberAddressMapper.selectByPrimaryKey(id));
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

        return just(ofNullable(memberAddressMapper.selectByPrimaryKey(id)));
    }

    /**
     * query address by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public List<MemberAddress> selectMemberAddressByMemberId(Long memberId) {
        LOGGER.info("List<MemberAddress> selectMemberAddressByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return memberAddressMapper.selectByMemberId(memberId);
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

        return just(memberAddressMapper.selectByMemberId(memberId));
    }

    /**
     * query address info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public List<MemberAddressInfo> selectMemberAddressInfoByMemberId(Long memberId) {
        LOGGER.info("List<MemberAddressInfo> selectMemberAddressInfoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return memberAddressMapper.selectByMemberId(memberId)
                .stream().map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO).collect(toList());
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

        return just(memberAddressMapper.selectByMemberId(memberId)
                .stream().map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO).collect(toList()));
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
                                .orElseGet(() ->
                                        error(() -> new BlueException(DATA_NOT_EXIST)))
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
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberAddressMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO))
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * select address by page and condition
     *
     * @param limit
     * @param rows
     * @param memberAddressCondition
     * @return
     */
    @Override
    public Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndCondition(Long limit, Long rows, MemberAddressCondition memberAddressCondition) {
        LOGGER.info("Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndCondition(Long limit, Long rows, MemberAddressCondition memberAddressCondition), " +
                "limit = {}, rows = {}, memberAddressCondition = {}", limit, rows, memberAddressCondition);
        if (isNull(limit) || limit < 0 || isNull(rows) || rows < 1)
            throw new BlueException(INVALID_PARAM);

        return just(memberAddressMapper.selectByLimitAndCondition(limit, rows, memberAddressCondition));
    }

    /**
     * count address by condition
     *
     * @param memberAddressCondition
     * @return
     */
    @Override
    public Mono<Long> countMemberAddressMonoByCondition(MemberAddressCondition memberAddressCondition) {
        LOGGER.info("Mono<Long> countMemberAddressMonoByCondition(MemberAddressCondition memberAddressCondition), memberCondition = {}", memberAddressCondition);
        return just(ofNullable(memberAddressMapper.countByCondition(memberAddressCondition)).orElse(0L));
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

        MemberAddressCondition memberAddressCondition = MEMBER_ADDRESS_CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectMemberAddressMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberAddressCondition), countMemberAddressMonoByCondition(memberAddressCondition))
                .flatMap(tuple2 -> {
                    List<MemberAddress> addresses = tuple2.getT1();
                    Mono<List<MemberAddressInfo>> memberAddressInfosMono = addresses.size() > 0 ?
                            just(addresses.stream()
                                    .map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return memberAddressInfosMono
                            .flatMap(memberAddressInfos ->
                                    just(new PageModelResponse<>(memberAddressInfos, tuple2.getT2())));
                });
    }

}
