package com.blue.member.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberCondition;
import com.blue.member.remote.consumer.RpcFinanceAccountServiceConsumer;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.mapper.MemberBasicMapper;
import com.blue.member.service.inter.MemberBasicService;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_INFO;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
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

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    private final RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBasicServiceImpl(MemberBasicMapper memberBasicMapper, BlueIdentityProcessor blueIdentityProcessor,
                                  RpcRoleServiceConsumer rpcRoleServiceConsumer,
                                  RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer) {
        this.memberBasicMapper = memberBasicMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
        this.rpcFinanceAccountServiceConsumer = rpcFinanceAccountServiceConsumer;
    }

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * is a number exist?
     */
    private final Consumer<MemberBasic> MEMBER_EXIST_VALIDATOR = mb -> {
        MemberBasic exist = memberBasicMapper.getByPhone(mb.getPhone());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The phone number already exists");

        exist = memberBasicMapper.getByEmail(mb.getEmail());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The email already exists");

        exist = memberBasicMapper.getByName(mb.getName());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The name already exists");
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberBasicSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Consumer<MemberCondition> CONDITION_REPACKAGER = condition -> {
        if (condition != null) {
            ofNullable(condition.getSortAttribute())
                    .filter(StringUtils::hasText)
                    .map(SORT_ATTRIBUTE_MAPPING::get)
                    .filter(StringUtils::hasText)
                    .ifPresent(condition::setSortAttribute);

            assertSortType(condition.getSortType(), true);

            ofNullable(condition.getName())
                    .filter(n -> !isBlank(n)).ifPresent(n -> condition.setName("%" + n + "%"));
        }
    };

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone), phone = {}", phone);
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        return just(ofNullable(memberBasicMapper.getByPhone(phone)));
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email), email = {}", email);
        if (email == null || "".equals(email))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email can't be blank''");
        return just(ofNullable(memberBasicMapper.getByEmail(email)));
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);
        return just(ofNullable(memberBasicMapper.selectByPrimaryKey(id)));
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(id)
                .flatMap(this::getMemberBasicMonoByPrimaryKey)
                .flatMap(mbOpt ->
                        mbOpt.map(Mono::just)
                                .orElseGet(() ->
                                        error(new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message)))
                ).flatMap(mb -> {
                    if (VALID.status != mb.getStatus())
                        return error(new BlueException(FORBIDDEN.status, FORBIDDEN.code, ACCOUNT_HAS_BEEN_FROZEN.message));
                    LOGGER.info("mb = {}", mb);
                    return just(mb);
                }).flatMap(mb ->
                        just(MEMBER_BASIC_2_MEMBER_INFO.apply(mb))
                );
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
    public void insert(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("void insert(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (memberRegistryParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        MEMBER_EXIST_VALIDATOR.accept(memberBasic);

        long id = blueIdentityProcessor.generate(MemberBasic.class);

        memberBasic.setId(id);
        memberBasic.setPassword(ENCODER.encode(memberBasic.getPassword()));

        //init default role
        rpcRoleServiceConsumer.insertDefaultMemberRoleRelation(id);

        //init finance account
        rpcFinanceAccountServiceConsumer.insertInitFinanceAccount(id);

        memberBasicMapper.insert(memberBasic);

        /*if (1 == 1) {
            throw new BlueException(500, 500, "test rollback");
        }*/
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

        if (isEmpty(ids))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "ids can't be empty");
        if (ids.size() > DB_SELECT.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "ids size can't be greater than " + DB_SELECT.value);

        return just(memberBasicMapper.selectByIds(ids));
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

        return this.countMemberBasicMonoByCondition(memberCondition)
                .flatMap(memberCount -> {
                    Mono<List<MemberBasic>> listMono = memberCount > 0L ? this.selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberCondition) : just(emptyList());
                    return zip(listMono, just(memberCount));
                })
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
