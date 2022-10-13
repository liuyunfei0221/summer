package com.blue.media.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist message template
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MessageTemplateUpdateParam extends MessageTemplateInsertParam {

    private static final long serialVersionUID = -824205978948363585L;
    
    private Long id;

    public MessageTemplateUpdateParam() {
    }

    public MessageTemplateUpdateParam(Long id, String name, String description, Integer type, Integer businessType, String title, String content) {
        super(name, description, type, businessType, title, content);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MessageTemplateUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
