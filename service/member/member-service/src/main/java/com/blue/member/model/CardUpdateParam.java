package com.blue.member.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a card
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class CardUpdateParam extends CardInsertParam {

    private static final long serialVersionUID = -3441737574837229266L;

    private Long id;

    public CardUpdateParam() {
    }

    public CardUpdateParam(Long id, String name, String detail, Long coverId, Long contentId, String extra) {
        super(name, detail, coverId, contentId, extra);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CardUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", coverId=" + coverId +
                ", contentId=" + contentId +
                ", extra='" + extra + '\'' +
                '}';
    }

}
