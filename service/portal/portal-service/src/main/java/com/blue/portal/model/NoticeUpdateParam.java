package com.blue.portal.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist notice
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class NoticeUpdateParam extends NoticeInsertParam {

    private static final long serialVersionUID = -7209271155508366825L;

    private Long id;

    public NoticeUpdateParam() {
    }

    public NoticeUpdateParam(Long id, String title, String content, String link, Integer type) {
        super(title, content, link, type);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
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
        return "NoticeUpdateParam{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }

}
