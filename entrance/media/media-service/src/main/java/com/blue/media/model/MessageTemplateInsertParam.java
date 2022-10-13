package com.blue.media.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageType;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new message template
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class MessageTemplateInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -7766338095091559323L;

    protected String name;

    protected String description;

    /**
     * @see com.blue.basic.constant.media.MessageType
     */
    protected Integer type;

    /**
     * @see com.blue.basic.constant.media.MessageBusinessType
     */
    protected Integer businessType;

    protected String title;

    protected String content;

    public MessageTemplateInsertParam() {
    }

    public MessageTemplateInsertParam(String name, String description, Integer type, Integer businessType, String title, String content) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.content = content;
    }

    @Override
    public void asserts() {
        int len;
        if (isBlank(this.name) || (len = this.name.length()) < (int) NAME_LEN_MIN.value || len > (int) NAME_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid name");
        if (isBlank(this.description) || (len = this.description.length()) < (int) DESCRIPTION_LEN_MIN.value || len > (int) DESCRIPTION_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid description");

        assertMessageType(this.type, false);
        assertMessageBusinessType(this.businessType, false);

        if (isBlank(this.title) || (len = this.title.length()) < (int) TITLE_LEN_MIN.value || len > (int) TITLE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.content) || (len = this.content.length()) < (int) CONTENT_LEN_MIN.value || len > (int) CONTENT_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid content");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
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

    @Override
    public String toString() {
        return "MessageTemplateInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
