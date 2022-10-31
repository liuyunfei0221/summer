package com.blue.marketing.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.*;
import com.blue.marketing.model.EventRecordManagerInfo;
import com.blue.marketing.model.RewardDateRelationInsertParam;
import com.blue.marketing.model.RewardInsertParam;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.RewardDateRelation;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
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

    public static final BiFunction<Reward, Map<Long, String>, RewardManagerInfo> REWARD_2_REWARD_MANAGER_INFO_CONVERTER = (reward, idAndMemberNameMapping) -> {
        if (isNull(reward))
            throw new BlueException(EMPTY_PARAM);

        return new RewardManagerInfo(reward.getId(), reward.getName(), reward.getDetail(), reward.getLink(), reward.getType(), reward.getData(), reward.getStatus(),
                reward.getCreateTime(), reward.getUpdateTime(), reward.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(reward.getCreator())).orElse(EMPTY_VALUE.value),
                reward.getUpdater(), ofNullable(idAndMemberNameMapping).map(m -> m.get(reward.getUpdater())).orElse(EMPTY_VALUE.value));
    };

    /**
     * reward date rel insert param -> reward date rel
     */
    public static final Function<RewardDateRelationInsertParam, RewardDateRelation> REWARD_DATE_REL_INSERT_PARAM_2_REWARD_DATE_REL_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        RewardDateRelation relation = new RewardDateRelation();

        relation.setRewardId(param.getRewardId());
        relation.setYear(param.getYear());
        relation.setMonth(param.getMonth());
        relation.setDay(param.getDay());
        relation.setCreateTime(stamp);
        relation.setUpdateTime(stamp);

        return relation;
    };

    /**
     * reward date rel -> reward date rel info
     */
    public static final BiFunction<RewardDateRelation, RewardInfo, RewardDateRelationInfo> REWARD_DATE_REL_2_REWARD_DATE_REL_INFO_CONVERTER = (relation, rewardInfo) -> {
        if (isNull(relation))
            throw new BlueException(EMPTY_PARAM);

        return new RewardDateRelationInfo(relation.getId(), relation.getRewardId(), rewardInfo, relation.getYear(), relation.getMonth(), relation.getDay());
    };

    /**
     * reward date rel -> reward date rel manager indo
     *
     * @param relation
     * @param rewardInfoIdAndNameMapping
     * @param idAndMemberNameMapping
     * @return
     */
    public static RewardDateRelationManagerInfo rewardDateRelToRewardDateRelManagerInfo(RewardDateRelation relation, Map<Long, RewardInfo> rewardInfoIdAndNameMapping, Map<Long, String> idAndMemberNameMapping) {
        if (isNull(relation))
            throw new BlueException(EMPTY_PARAM);

        return new RewardDateRelationManagerInfo(relation.getId(), relation.getRewardId(), rewardInfoIdAndNameMapping.get(relation.getRewardId()), relation.getYear(), relation.getMonth(), relation.getDay(),
                relation.getCreateTime(), relation.getUpdateTime(), relation.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(relation.getCreator())).orElse(EMPTY_VALUE.value),
                relation.getUpdater(), ofNullable(idAndMemberNameMapping).map(m -> m.get(relation.getUpdater())).orElse(EMPTY_VALUE.value));
    }

    /**
     * event record -> event record info
     */
    public static final Function<EventRecord, EventRecordInfo> EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER = eventRecord -> {
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        return new EventRecordInfo(eventRecord.getId(), eventRecord.getMemberId(),
                eventRecord.getType(), eventRecord.getData(), eventRecord.getStatus(), eventRecord.getCreateTime());
    };

    /**
     * event record -> event record manager info
     */
    public static final BiFunction<EventRecord, String, EventRecordManagerInfo> EVENT_RECORD_2_EVENT_RECORD_MANAGER_INFO_CONVERTER = (eventRecord, memberName) -> {
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        return new EventRecordManagerInfo(eventRecord.getId(), eventRecord.getMemberId(), isNotBlank(memberName) ? memberName : EMPTY_VALUE.value,
                eventRecord.getType(), eventRecord.getData(), eventRecord.getStatus(), eventRecord.getCreateTime());
    };

}
