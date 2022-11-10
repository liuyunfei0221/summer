package com.blue.performance.service.shine;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.member.BlueMemberThreshold.EXTRA_LEN_MAX;
import static com.blue.basic.constant.member.BlueMemberThreshold.EXTRA_LEN_MIN;

/**
 * params for insert a new shine
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class ShineInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 8269418134228197108L;

    protected String title;

    protected String content;

    protected String detail;

    protected String contact;

    protected String contactDetail;

    protected Long cityId;

    protected String addressDetail;

    protected String extra;

    protected Integer priority;

    public ShineInsertParam() {
    }

    public ShineInsertParam(String title, String content, String detail, String contact, String contactDetail, Long cityId, String addressDetail, String extra, Integer priority) {
        this.title = title;
        this.content = content;
        this.detail = detail;
        this.contact = contact;
        this.contactDetail = contactDetail;
        this.cityId = cityId;
        this.addressDetail = addressDetail;
        this.extra = extra;
        this.priority = priority;
    }

    @Override
    public void asserts() {
        int len;
        if (isBlank(this.title) || (len = this.title.length()) < (int) TITLE_LEN_MIN.value || len > (int) TITLE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.content) || (len = this.content.length()) < (int) CONTENT_LEN_MIN.value || len > (int) CONTENT_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid content");
        if (isNotBlank(this.detail) && ((len = this.detail.length()) < (int) DETAIL_LEN_MIN.value || len > (int) DETAIL_LEN_MAX.value))
            throw new BlueException(INVALID_PARAM);
        if (isNotBlank(this.contact) && ((len = this.contact.length()) < (int) TITLE_LEN_MIN.value || len > (int) TITLE_LEN_MAX.value))
            throw new BlueException(INVALID_PARAM);
        if (isNotBlank(this.contactDetail) && ((len = this.contactDetail.length()) < (int) CONTENT_LEN_MIN.value || len > (int) CONTENT_LEN_MAX.value))
            throw new BlueException(INVALID_PARAM);
        if (isNull(this.cityId))
            throw new BlueException(INVALID_PARAM);
        if (isNotBlank(this.extra) && ((len = this.extra.length()) < (int) EXTRA_LEN_MIN.value || len > (int) EXTRA_LEN_MAX.value))
            throw new BlueException(INVALID_PARAM);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "ShineInsertParam{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", detail='" + detail + '\'' +
                ", contact='" + contact + '\'' +
                ", contactDetail='" + contactDetail + '\'' +
                ", cityId=" + cityId +
                ", addressDetail='" + addressDetail + '\'' +
                ", extra='" + extra + '\'' +
                ", priority=" + priority +
                '}';
    }

}
