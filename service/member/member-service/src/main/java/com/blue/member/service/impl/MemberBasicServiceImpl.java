package com.blue.member.service.impl;

import com.blue.base.common.base.Check;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberCondition;
import com.blue.member.remote.consumer.RpcControlServiceConsumer;
import com.blue.member.remote.consumer.RpcFinanceAccountServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.mapper.MemberBasicMapper;
import com.blue.member.service.inter.MemberBasicService;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Check.*;
import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_INFO;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * member basic service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberBasicServiceImpl implements MemberBasicService {

    private static final Logger LOGGER = getLogger(MemberBasicServiceImpl.class);

    private MemberBasicMapper memberBasicMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RpcControlServiceConsumer rpcControlServiceConsumer;

    private final RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBasicServiceImpl(MemberBasicMapper memberBasicMapper, BlueIdentityProcessor blueIdentityProcessor,
                                  RpcControlServiceConsumer rpcControlServiceConsumer,
                                  RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer) {
        this.memberBasicMapper = memberBasicMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rpcControlServiceConsumer = rpcControlServiceConsumer;
        this.rpcFinanceAccountServiceConsumer = rpcFinanceAccountServiceConsumer;
    }

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * is a number exist?
     */
    private final Consumer<MemberRegistryParam> MEMBER_EXIST_VALIDATOR = mrp -> {
        MemberBasic exist = memberBasicMapper.selectByPhone(mrp.getPhone());
        if (isNotNull(exist))
            throw new BlueException(PHONE_ALREADY_EXIST);

        exist = memberBasicMapper.selectByEmail(mrp.getEmail());
        if (isNotNull(exist))
            throw new BlueException(EMAIL_ALREADY_EXIST);

        exist = memberBasicMapper.selectByName(mrp.getName());
        if (isNotNull(exist))
            throw new BlueException(NAME_ALREADY_EXIST);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberBasicSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Consumer<MemberCondition> CONDITION_REPACKAGER = condition -> {
        if (condition != null) {
            ofNullable(condition.getSortAttribute())
                    .filter(Check::isNotBlank)
                    .map(SORT_ATTRIBUTE_MAPPING::get)
                    .filter(Check::isNotBlank)
                    .ifPresent(condition::setSortAttribute);

            assertSortType(condition.getSortType(), true);

            ofNullable(condition.getName())
                    .filter(Check::isNotBlank).ifPresent(n -> condition.setName("%" + n + "%"));
        }
    };

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByPhone(String phone) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone), phone = {}", phone);
        if (isNotBlank(phone))
            return just(ofNullable(memberBasicMapper.selectByPhone(phone)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByEmail(String email) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email), email = {}", email);
        if (isNotBlank(email))
            return just(ofNullable(memberBasicMapper.selectByEmail(email)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return just(ofNullable(memberBasicMapper.selectByPrimaryKey(id)));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberInfo> selectMemberInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isValidIdentity(id))
            return just(id)
                    .flatMap(this::selectMemberBasicMonoByPrimaryKey)
                    .flatMap(mbOpt ->
                            mbOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(UNAUTHORIZED)))
                    ).flatMap(mb -> {
                        if (isInvalidStatus(mb.getStatus()))
                            return error(() -> new BlueException(ACCOUNT_HAS_BEEN_FROZEN));
                        LOGGER.info("mb = {}", mb);
                        return just(mb);
                    }).flatMap(mb ->
                            just(MEMBER_BASIC_2_MEMBER_INFO.apply(mb))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * member registry
     *
     * @param memberRegistryParam
     * @return
     */
    @SuppressWarnings("CommentedOutCode")
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 3, lockRetryTimes = 2, timeoutMills = 15000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 150000)
    @GlobalLock
    public MemberInfo insertMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("void insert(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_EXIST_VALIDATOR.accept(memberRegistryParam);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);
        long id = blueIdentityProcessor.generate(MemberBasic.class);

        memberBasic.setId(id);
        memberBasic.setPassword(ENCODER.encode(memberBasic.getPassword()));

        //init default role
        rpcControlServiceConsumer.insertDefaultMemberRoleRelation(id);

        //init finance account
        rpcFinanceAccountServiceConsumer.insertInitFinanceAccount(id);

        memberBasicMapper.insert(memberBasic);

        /*if (1 == 1) {
            throw new BlueException(500, 500, "test rollback");
        }*/
        return MEMBER_BASIC_2_MEMBER_INFO.apply(memberBasic);
    }

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberBasicMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * select member by page and condition
     *
     * @param limit
     * @param rows
     * @param memberCondition
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberCondition memberCondition) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberCondition memberCondition), " +
                "limit = {}, rows = {}, memberCondition = {}", limit, rows, memberCondition);
        return just(memberBasicMapper.selectByLimitAndCondition(limit, rows, memberCondition));
    }

    /**
     * count member by condition
     *
     * @param memberCondition
     * @return
     */
    @Override
    public Mono<Long> countMemberBasicMonoByCondition(MemberCondition memberCondition) {
        LOGGER.info("Mono<Long> countMemberBasicMonoByCondition(MemberCondition memberCondition), memberCondition = {}", memberCondition);
        return just(ofNullable(memberBasicMapper.countByCondition(memberCondition)).orElse(0L));
    }

    /**
     * select member info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberInfo>> selectMemberInfoPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberInfo>> selectMemberInfoPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        MemberCondition memberCondition = pageModelRequest.getParam();
        CONDITION_REPACKAGER.accept(memberCondition);

        return zip(selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberCondition), countMemberBasicMonoByCondition(memberCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberInfo>> memberInfosMono = members.size() > 0 ?
                            just(members.stream()
                                    .map(MEMBER_BASIC_2_MEMBER_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return memberInfosMono
                            .flatMap(memberInfos ->
                                    just(new PageModelResponse<>(memberInfos, tuple2.getT2())));
                });
    }

}
