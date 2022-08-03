package com.blue.marketing.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update exist reward date relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class RewardDateRelationUpdateParam extends RewardDateRelationInsertParam {

    private static final long serialVersionUID = 3709726547884800171L;

    private Long id;

    public RewardDateRelationUpdateParam() {
    }

    public RewardDateRelationUpdateParam(Long id, Long rewardId, Integer year, Integer month, Integer day) {
        super(rewardId, year, month, day);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
    }

    public Long getId() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RewardDateRelationUpdateParam{" +
                "id=" + id +
                ", rewardId=" + rewardId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

}
