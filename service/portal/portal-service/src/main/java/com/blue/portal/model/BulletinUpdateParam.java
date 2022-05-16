package com.blue.portal.model;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

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
        super.asserts();
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
        return "BulletinInsertParam{" +
                "id=" + id +
                ", title='" + super.getTitle() + '\'' +
                ", content='" + super.getContent() + '\'' +
                ", link='" + super.getLink() + '\'' +
                ", type=" + super.getType() +
                ", priority=" + super.getPriority() +
                ", activeTime=" + super.getActiveTime() +
                ", expireTime=" + super.getExpireTime() +
                '}';
    }

}
