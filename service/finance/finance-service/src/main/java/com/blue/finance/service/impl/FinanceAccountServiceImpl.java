package com.blue.finance.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.mapper.FinanceAccountMapper;
import com.blue.finance.service.inter.FinanceAccountService;
import com.blue.identity.common.BlueIdentityProcessor;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.time.Instant;
import java.util.Optional;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * 用户业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {

    private static final Logger LOGGER = getLogger(FinanceAccountServiceImpl.class);

    private final FinanceAccountMapper financeAccountMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FinanceAccountServiceImpl(FinanceAccountMapper financeAccountMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.financeAccountMapper = financeAccountMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * 新增资金账户
     *
     * @param financeAccount
     */
    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    @Override
    public void insertFinanceAccount(FinanceAccount financeAccount) {
        LOGGER.info("insertFinanceAccount(FinanceAccount financeAccount), financeAccount = {}", financeAccount);
        financeAccountMapper.insert(financeAccount);
//        if (1 == 1) {
//            throw new BlueException(500, 500, "测试异常回滚");
//        }
    }

    @GlobalTransactional
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    @Override
    public void insertInitFinanceAccount(Long memberId) {
        LOGGER.info("insertInitFinanceAccount(Long memberId), memberId = {}", memberId);

        if (memberId == null || memberId < 1L) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberId不能为空或小于1");
        }

        long epochSecond = Instant.now().getEpochSecond();

        FinanceAccount financeAccount = new FinanceAccount();

        long id = blueIdentityProcessor.generate(FinanceAccount.class);
        financeAccount.setId(id);

        financeAccount.setMemberId(memberId);
        financeAccount.setBalance(0L);
        financeAccount.setFrozen(0L);
        financeAccount.setIncome(0L);
        financeAccount.setOutlay(0L);
        financeAccount.setStatus(Status.VALID.status);
        financeAccount.setCreateTime(epochSecond);
        financeAccount.setUpdateTime(epochSecond);

        insertFinanceAccount(financeAccount);
    }

    /**
     * 根据成员主键获取资金账户余额信息
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<FinanceAccount> getFinanceAccountByMemberId(Long memberId) {
        LOGGER.info("getFinanceAccountByMemberId(Long memberId), memberId = {}", memberId);

        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员主键不能为空或小于1");

        return ofNullable(financeAccountMapper.selectByMemberId(memberId));
    }

}
