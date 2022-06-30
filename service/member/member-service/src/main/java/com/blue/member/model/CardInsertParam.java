package com.blue.member.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.base.constant.member.BlueMemberThreshold.*;

/**
 * params for insert a new card
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class CardInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 7924829796011310024L;

    private String name;

    private String detail;

    /**
     * cover attachment id
     */
    private Long coverId;

    /**
     * content attachment id
     */
    private Long contentId;

    private String extra;

    public CardInsertParam() {
    }

    public CardInsertParam(String name, String detail, Long coverId, Long contentId, String extra) {
        this.name = name;
        this.detail = detail;
        this.coverId = coverId;
        this.contentId = contentId;
        this.extra = extra;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(INVALID_PARAM);

        int length = name.length();
        if (length < NAME_LEN_MIN.value || length > NAME_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = detail.length();
        if (length < ADDR_DETAIL_LEN_MIN.value || length > ADDR_DETAIL_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        if (isInvalidIdentity(this.contentId))
            throw new BlueException(INVALID_PARAM);

        length = extra.length();
        if (length < EXTRA_LEN_MIN.value || length > EXTRA_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "CardInsertParam{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", coverId=" + coverId +
                ", contentId=" + contentId +
                ", extra='" + extra + '\'' +
                '}';
    }

}
