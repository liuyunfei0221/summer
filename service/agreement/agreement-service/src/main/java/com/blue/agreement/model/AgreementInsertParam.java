package com.blue.agreement.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertAgreementType;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new agreement
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AgreementInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 15008478386309529L;

    protected String title;

    protected String content;

    protected String link;

    protected Integer type;

    protected Integer status;

    protected Integer priority;

    public AgreementInsertParam() {
    }

    public AgreementInsertParam(String title, String content, String link, Integer type, Integer status, Integer priority) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
        this.status = status;
        this.priority = priority;
    }

    @Override
    public void asserts() {
        if (isBlank(this.title))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "title can't be blank");
        if (isBlank(this.content))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "content can't be blank");
        if (isBlank(this.link))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "link can't be blank");
        assertAgreementType(this.type, false);
        assertStatus(this.status, false);
        if (isNull(this.priority))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "priority can't be blank");
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "AgreementInsertParam{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }

}
