package com.blue.marketing.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.api.model.RewardManagerInfo;
import com.blue.marketing.model.RewardInsertParam;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.repository.entity.Reward;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Status.VALID;
import static java.util.Optional.ofNullable;

/**
 * model converters in marketing project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
public final class MarketingModelConverters {

    /**
     * reward insert param -> reward
     */
    public static final Function<RewardInsertParam, Reward> REWARD_INSERT_PARAM_2_REWARD_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Reward reward = new Reward();

        reward.setName(param.getName());
        reward.setDetail(param.getDetail());
        reward.setLink(param.getLink());
        reward.setType(param.getType());
        reward.setData(param.getData());
        reward.setStatus(VALID.status);
        reward.setCreateTime(stamp);
        reward.setUpdateTime(stamp);

        return reward;
    };

    /**
     * reward -> reward info
     */
    public static final Function<Reward, RewardInfo> REWARD_2_REWARD_INFO_CONVERTER = reward -> {
        if (isNull(reward))
            throw new BlueException(EMPTY_PARAM);

        return new RewardInfo(reward.getId(), reward.getName(), reward.getDetail(), reward.getLink(), reward.getType(), reward.getData());
    };

    /**
     * reward -> reward manager indo
     *
     * @param reward
     * @param idAndMemberNameMapping
     * @return
     */
    public static RewardManagerInfo rewardToRewardManagerInfo(Reward reward, Map<Long, String> idAndMemberNameMapping) {
        if (isNull(reward))
            throw new BlueException(EMPTY_PARAM);

        return new RewardManagerInfo(reward.getId(), reward.getName(), reward.getDetail(), reward.getLink(), reward.getType(), reward.getData(), reward.getStatus(),
                reward.getCreateTime(), reward.getUpdateTime(), reward.getCreator(), ofNullable(idAndMemberNameMapping.get(reward.getCreator())).orElse(EMPTY_DATA.value),
                reward.getUpdater(), ofNullable(idAndMemberNameMapping.get(reward.getUpdater())).orElse(EMPTY_DATA.value));
    }

    /**
     * event record -> event record info
     */
    public static final BiFunction<EventRecord, String, EventRecordInfo> EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER = (eventRecord, creatorName) -> {
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        return new EventRecordInfo(eventRecord.getId(), eventRecord.getType(),
                eventRecord.getData(), eventRecord.getStatus(), eventRecord.getCreateTime(), eventRecord.getCreator(), isNotBlank(creatorName) ? creatorName : EMPTY_DATA.value);
    };

}
