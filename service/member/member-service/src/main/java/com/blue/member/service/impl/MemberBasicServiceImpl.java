package com.blue.member.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryInfo;
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
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 用户业务实现
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
        MemberBasic exist = memberBasicMapper.selectByPhone(mb.getPhone());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "手机号已存在");

        exist = memberBasicMapper.selectByEmail(mb.getEmail());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "邮箱地址已存在");

        exist = memberBasicMapper.selectByName(mb.getName());
        if (exist != null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "昵称已存在");
    };

    /**
     * 根据手机号获取成员信息
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByPhone(String phone) {
        LOGGER.info("getByPhone(String phone), phone = {}", phone);
        if (phone == null || "".equals(phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone不能为空或''");
        LOGGER.info("phone = {}", phone);
        return just(ofNullable(memberBasicMapper.selectByPhone(phone)));
    }

    /**
     * 根据邮箱获取成员信息
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByEmail(String email) {
        LOGGER.info("getByEmail(String email), email = {}", email);
        if (email == null || "".equals(email))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email不能为空或''");
        LOGGER.info("email = {}", email);
        return just(ofNullable(memberBasicMapper.selectByEmail(email)));
    }

    /**
     * 根据主键获取成员信息
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getByPrimaryKey(Long id) {
        LOGGER.info("getByPrimaryKey(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员主键不能为空或小于1");
        return just(ofNullable(memberBasicMapper.selectByPrimaryKey(id)));
    }

    /**
     * 根据主键获取用户信息并校验
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberInfo> getMemberInfoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("getVoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员主键不能为空或小于1");

        return just(id)
                .flatMap(this::getByPrimaryKey)
                .flatMap(mbOpt ->
                        mbOpt.map(Mono::just)
                                .orElseGet(() ->
                                        error(new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message)))
                ).flatMap(mb -> {
                    if (VALID.status != mb.getStatus())
                        return error(new BlueException(FORBIDDEN.status, FORBIDDEN.code, "账号已冻结"));
                    LOGGER.info("mb = {}", mb);
                    return just(mb);
                }).flatMap(mb ->
                        just(new MemberInfo(mb.getId(), mb.getName(), mb.getIcon(), mb.getGender()))
                );
    }

    /**
     * 注册账户
     *
     * @param memberRegistryInfo
     * @return
     */
    @SuppressWarnings("CommentedOutCode")
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED, rollbackFor = Exception.class,
            lockRetryInternal = 3, lockRetryTimes = 2, timeoutMills = 15000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 150000)
    @GlobalLock
    public void insert(MemberRegistryInfo memberRegistryInfo) {
        LOGGER.info("memberRegistryDTO = {}", memberRegistryInfo);
        if (memberRegistryInfo == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "注册信息不能为空");

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryInfo);

        //校验
        MEMBER_EXIST_VALIDATOR.accept(memberBasic);

        long id = blueIdentityProcessor.generate(MemberBasic.class);

        memberBasic.setId(id);
        memberBasic.setPassword(ENCODER.encode(memberBasic.getPassword()));

        //初始化默认角色
        rpcRoleServiceConsumer.insertDefaultMemberRoleRelation(id);

        //初始化资金账户
        rpcFinanceAccountServiceConsumer.insertInitFinanceAccount(id);

        //添加成员基础信息
        memberBasicMapper.insert(memberBasic);

        /*if (1 == 1) {
            throw new BlueException(500, 500, "测试异常回滚");
        }*/
    }

    /**
     * 查询用户
     *
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMember() {
        return just(memberBasicMapper.selectByCondition(null));
    }
}
