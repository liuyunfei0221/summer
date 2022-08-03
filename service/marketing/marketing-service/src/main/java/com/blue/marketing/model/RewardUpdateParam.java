package com.blue.marketing.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist reward
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class RewardUpdateParam extends RewardInsertParam {

    private static final long serialVersionUID = 3709726547884800171L;

    private Long id;

    public RewardUpdateParam() {
    }

    public RewardUpdateParam(Long id, String name, String detail, String link, Integer type, String data) {
        super(name, detail, link, type, data);
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
        return "RewardUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }

}
