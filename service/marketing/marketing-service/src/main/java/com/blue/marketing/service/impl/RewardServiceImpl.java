package com.blue.marketing.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.repository.mapper.RewardMapper;
import com.blue.marketing.repository.mapper.SignRewardTodayRelationMapper;
import com.blue.marketing.service.inter.RewardService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Collections.emptyList;
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
        if (id == null || id < 1L)
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
    public List<Reward> listRewardByIds(List<Long> ids) {
        LOGGER.info("listRewardByIds(List<Long> ids), ids = {}", ids);
        if (CollectionUtils.isEmpty(ids))
            return emptyList();

        return rewardMapper.listRewardByIds(ids);
    }

    /**
     * select reward-date-relation by date
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<SignRewardTodayRelation> listRelationByYearAndMonth(Integer year, Integer month) {
        LOGGER.info("listRelationByYearAndMonth(Integer year, Integer month), year = {}, month = {}", year, month);
        if (year == null || month == null || year < 1 || month < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "year or month can't be null or less than 1");

        return signRewardTodayRelationMapper.listRelationByYearAndMonth(year, month);
    }

}
