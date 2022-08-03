package com.blue.marketing.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new reward
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RewardInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -4099297009366670202L;

    protected String name;

    protected String detail;

    protected String link;

    /**
     * reward type
     *
     * @see com.blue.basic.constant.marketing.RewardType
     */
    protected Integer type;

    /**
     * reward json
     */
    protected String data;

    public RewardInsertParam() {
    }

    public RewardInsertParam(String name, String detail, String link, Integer type, String data) {
        this.name = name;
        this.detail = detail;
        this.link = link;
        this.type = type;
        this.data = data;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.detail))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "detail can't be blank");
        if (isBlank(this.link))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "link can't be blank");
        if (isNull(this.type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");
        if (isBlank(this.data))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data can't be blank");
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RewardInsertParam{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }

}
