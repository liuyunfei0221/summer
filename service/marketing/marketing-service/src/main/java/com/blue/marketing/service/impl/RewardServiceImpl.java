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

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "ids can't be empty or size can't be greater than " + DB_SELECT.value);

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

        return signRewardTodayRelationMapper.selectByYearAndMonth(year, month);
    }

}
