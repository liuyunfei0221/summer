package com.blue.marketing.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.repository.mapper.RewardMapper;
import com.blue.marketing.repository.mapper.SignRewardTodayRelationMapper;
import com.blue.marketing.service.inter.RewardService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.base.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * reward service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RewardServiceImpl implements RewardService {

    private static final Logger LOGGER = getLogger(RewardServiceImpl.class);

    private final RewardMapper rewardMapper;

    private final SignRewardTodayRelationMapper signRewardTodayRelationMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RewardServiceImpl(RewardMapper rewardMapper, SignRewardTodayRelationMapper signRewardTodayRelationMapper) {
        this.rewardMapper = rewardMapper;
        this.signRewardTodayRelationMapper = signRewardTodayRelationMapper;
    }

    /**
     * select reward by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Reward> getReward(Long id) {
        LOGGER.info("Optional<Reward> getRewardByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(rewardMapper.selectByPrimaryKey(id));
    }

    /**
     * select rewards by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Reward> selectRewardByIds(List<Long> ids) {
        LOGGER.info("List<Reward> listRewardByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(rewardMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select reward-date-relation by date
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<SignRewardTodayRelation> selectRelationByYearAndMonth(Integer year, Integer month) {
        LOGGER.info("List<SignRewardTodayRelation> selectRelationByYearAndMonth(Integer year, Integer month), year = {}, month = {}", year, month);
        if (isNull(year) || isNull(month) || year < 1 || month < 1)
            throw new BlueException(BAD_REQUEST);

        return signRewardTodayRelationMapper.selectByYearAndMonth(year, month);
    }

}
