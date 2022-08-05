package com.blue.portal.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist bulletin
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class BulletinUpdateParam extends BulletinInsertParam {

    private static final long serialVersionUID = -7209271155508366825L;

    private Long id;

    public BulletinUpdateParam() {
    }

    public BulletinUpdateParam(Long id, String title, String content, String link, Integer type, Integer priority, Long activeTime, Long expireTime) {
        super(title, content, link, type, priority, activeTime, expireTime);
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
        return "BulletinUpdateParam{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", activeTime=" + activeTime +
                ", expireTime=" + expireTime +
                '}';
    }

}
