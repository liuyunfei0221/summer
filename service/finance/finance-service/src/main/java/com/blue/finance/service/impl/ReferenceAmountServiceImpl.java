package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.repository.entity.ReferenceAmount;
import com.blue.finance.repository.mapper.ReferenceAmountMapper;
import com.blue.finance.service.inter.ReferenceAmountService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * reference amount service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class ReferenceAmountServiceImpl implements ReferenceAmountService {

    private static final Logger LOGGER = getLogger(ReferenceAmountServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ReferenceAmountMapper referenceAmountMapper;

    public ReferenceAmountServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ReferenceAmountMapper referenceAmountMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.referenceAmountMapper = referenceAmountMapper;
    }

    /**
     * insert reference amount
     *
     * @param referenceAmount
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public ReferenceAmount insertReferenceAmount(ReferenceAmount referenceAmount) {
        LOGGER.info("ReferenceAmount insertReferenceAmount(ReferenceAmount referenceAmount), referenceAmount = {}", referenceAmount);
        if (isNull(referenceAmount))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(referenceAmount.getId()))
            referenceAmount.setId(blueIdentityProcessor.generate(ReferenceAmount.class));

        referenceAmountMapper.insert(referenceAmount);

        return referenceAmount;
    }

    /**
     * insert reference amounts
     *
     * @param referenceAmounts
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public List<ReferenceAmount> insertReferenceAmounts(List<ReferenceAmount> referenceAmounts) {
        LOGGER.info("List<ReferenceAmount> insertReferenceAmounts(List<ReferenceAmount> referenceAmounts), referenceAmounts = {}", referenceAmounts);
        if (isEmpty(referenceAmounts))
            return emptyList();

        for (ReferenceAmount referenceAmount : referenceAmounts)
            if (isInvalidIdentity(referenceAmount.getId()))
                referenceAmount.setId(blueIdentityProcessor.generate(ReferenceAmount.class));

        int inserted = referenceAmountMapper.insertBatch(referenceAmounts);
        LOGGER.info("inserted = {}", inserted);

        return referenceAmounts;
    }

    /**
     * update a exist reference amount
     *
     * @param referenceAmount
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean updateReferenceAmount(ReferenceAmount referenceAmount) {
        LOGGER.info("Boolean updateReferenceAmount(ReferenceAmount referenceAmount), referenceAmount = {}", referenceAmount);
        if (isNull(referenceAmount))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(referenceAmount.getId()))
            throw new BlueException(INVALID_IDENTITY);

        int updated = referenceAmountMapper.updateByPrimaryKeySelective(referenceAmount);
        LOGGER.info("referenceAmount = {}, updated = {}", referenceAmount, updated);

        return updated > 0;
    }

    /**
     * delete reference amount
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean deleteReferenceAmount(Long id) {
        LOGGER.info("Boolean deleteReferenceAmount(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        int deleted = referenceAmountMapper.deleteByPrimaryKey(id);
        LOGGER.info("deleted = {}", deleted);

        return deleted > 0;
    }

    /**
     * get reference amount by id
     *
     * @param id
     * @return
     */
    public Optional<ReferenceAmount> getReferenceAmount(Long id) {
        LOGGER.info("Optional<ReferenceAmount> getReferenceAmount(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(referenceAmountMapper.selectByPrimaryKey(id));
    }

    /**
     * get reference amount mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<ReferenceAmount> getReferenceAmountMono(Long id) {
        LOGGER.info("Mono<ReferenceAmount> getReferenceAmountMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(referenceAmountMapper.selectByPrimaryKey(id));
    }

    /**
     * select reference amounts by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<ReferenceAmount> selectReferenceAmountByIds(List<Long> ids) {
        LOGGER.info("List<ReferenceAmount> selectReferenceAmountByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();

        return referenceAmountMapper.selectByIds(ids);
    }

    /**
     * select reference amounts mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<ReferenceAmount>> selectReferenceAmountMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<ReferenceAmount>> selectReferenceAmountMonoByIds(List<Long> ids), ids = {}", ids);

        return just(selectReferenceAmountByIds(ids));
    }

}
