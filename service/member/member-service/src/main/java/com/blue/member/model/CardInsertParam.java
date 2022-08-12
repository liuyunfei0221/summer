package com.blue.member.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DETAIL_LEN_MAX;
import static com.blue.basic.constant.common.BlueCommonThreshold.DETAIL_LEN_MIN;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.member.BlueMemberThreshold.*;

/**
 * params for insert a new card
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class CardInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 7924829796011310024L;

    protected String name;

    protected String detail;

    /**
     * cover attachment id
     */
    protected Long coverId;

    /**
     * content attachment id
     */
    protected Long contentId;

    protected String extra;

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
        int len;
        if (isBlank(this.name) || (len = this.name.length()) < (int) NAME_LEN_MIN.value || len > (int) NAME_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        if (isNotBlank(this.detail) && ((len = this.detail.length()) < (int) DETAIL_LEN_MIN.value || len > (int) DETAIL_LEN_MAX.value))
            throw new BlueException(INVALID_PARAM);

        if (isInvalidIdentity(this.contentId))
            throw new BlueException(INVALID_PARAM);

        if (isNotBlank(this.extra) && ((len = this.extra.length()) < (int) EXTRA_LEN_MIN.value || len > (int) EXTRA_LEN_MAX.value))
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
