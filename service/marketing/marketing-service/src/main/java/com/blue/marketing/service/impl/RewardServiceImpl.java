package com.blue.marketing.service.impl;

import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.repository.mapper.RewardMapper;
import com.blue.marketing.repository.mapper.SignRewardTodayRelationMapper;
import com.blue.marketing.service.inter.RewardService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.CommonException.*;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * reward service impl
 *
 * @author DarkBlue
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
    public Optional<Reward> getRewardByPrimaryKey(Long id) {
        LOGGER.info("getRewardByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw INVALID_IDENTITY_EXP.exp;

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
        if (isInvalidIdentitiesWithMaxRows(ids, DB_SELECT.value))
            throw INVALID_IDENTITIES_EXP.exp;

        return rewardMapper.selectByIds(ids);
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
        LOGGER.info("listRelationByYearAndMonth(Integer year, Integer month), year = {}, month = {}", year, month);
        if (isNull(year) || isNull(month) || year < 1 || month < 1)
            throw BAD_REQUEST_EXP.exp;

        return signRewardTodayRelationMapper.selectByYearAndMonth(year, month);
    }

}
