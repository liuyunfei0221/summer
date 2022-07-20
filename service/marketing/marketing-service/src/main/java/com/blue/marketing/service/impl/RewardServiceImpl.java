package com.blue.marketing.service.impl;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.api.model.RewardManagerInfo;
import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.model.RewardInsertParam;
import com.blue.marketing.model.RewardUpdateParam;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.mapper.RewardMapper;
import com.blue.marketing.service.inter.RewardService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.ResponseElement.PAYLOAD_TOO_LARGE;
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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RewardServiceImpl(RewardMapper rewardMapper) {
        this.rewardMapper = rewardMapper;
    }

    @Override
    public RewardInfo insertReward(RewardInsertParam rewardInsertParam, Long operatorId) {
        return null;
    }

    @Override
    public RewardInfo updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId) {
        return null;
    }

    @Override
    public RewardInfo deleteReward(Long id) {
        return null;
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

    @Override
    public Mono<Reward> getRewardMono(Long id) {
        return null;
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

    @Override
    public Mono<List<Reward>> selectRewardMonoByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Mono<List<Reward>> selectReward() {
        return null;
    }

    @Override
    public Mono<List<Reward>> selectRewardMonoByLimitAndCondition(Long limit, Long rows, RewardCondition rewardCondition) {
        return null;
    }

    @Override
    public Mono<Long> countRewardMonoByCondition(RewardCondition rewardCondition) {
        return null;
    }

    @Override
    public Mono<PageModelResponse<RewardManagerInfo>> selectRewardManagerInfoPageMonoByPageAndCondition(PageModelRequest<RewardCondition> pageModelRequest) {
        return null;
    }
}
