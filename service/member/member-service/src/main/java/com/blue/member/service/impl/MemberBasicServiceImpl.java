package com.blue.member.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
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
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
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
     * 成员校验器
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

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByPhone(String phone) {
        LOGGER.info("getByPhone(String phone), phone = {}", phone);
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        LOGGER.info("phone = {}", phone);
        return just(ofNullable(memberBasicMapper.getByPhone(phone)));
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByEmail(String email) {
        LOGGER.info("getByEmail(String email), email = {}", email);
        if (email == null || "".equals(email))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email can't be blank''");
        LOGGER.info("email = {}", email);
        return just(ofNullable(memberBasicMapper.getByEmail(email)));
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByPrimaryKey(Long id) {
        LOGGER.info("getByPrimaryKey(Long id), id = {}", id);
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
    public Mono<MemberInfo> getMemberInfoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("getVoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(id)
                .flatMap(this::getByPrimaryKey)
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
                        just(new MemberInfo(mb.getId(), mb.getName(), mb.getIcon(), mb.getGender()))
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
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED, rollbackFor = Exception.class,
            lockRetryInternal = 3, lockRetryTimes = 2, timeoutMills = 15000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 150000)
    @GlobalLock
    public void insert(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("memberRegistryDTO = {}", memberRegistryParam);
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
     * select member
     *
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMember() {
        return just(memberBasicMapper.select());
    }

    /**
     * select member by condition
     *
     * @param memberCondition
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMemberByLimitAndCondition(Long limit, Long rows, MemberCondition memberCondition) {
        return null;
    }
}
