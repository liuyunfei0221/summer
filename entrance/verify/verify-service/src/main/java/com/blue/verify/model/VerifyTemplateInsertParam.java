package com.blue.verify.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new verify template
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class VerifyTemplateInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -3640635279642897437L;

    protected String name;

    protected String description;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    protected String type;

    /**
     * @see com.blue.basic.constant.verify.VerifyBusinessType
     */
    protected String businessType;

    protected String title;

    protected String content;

    public VerifyTemplateInsertParam() {
    }

    public VerifyTemplateInsertParam(String name, String description, String type, String businessType, String title, String content) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.content = content;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid name");
        if (isBlank(this.description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid description");
        assertVerifyType(this.type, false);
        assertVerifyBusinessType(this.businessType, false);
        if (isBlank(this.title))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.content))
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
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
        return "VerifyTemplateInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", businessType='" + businessType + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
