package com.blue.shine.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a shine
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ShineUpdateParam extends ShineInsertParam {

    private static final long serialVersionUID = 5116988790934094716L;

    private Long id;

    public ShineUpdateParam() {
    }

    public ShineUpdateParam(Long id, String title, String content, String detail, String contact, String contactDetail, Long cityId, String addressDetail, String extra, Integer order) {
        super(title, content, detail, contact, contactDetail, cityId, addressDetail, extra, order);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ShineUpdateParam{" +
                "id=" + id +
                ", title='" + title + '\'' +
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
