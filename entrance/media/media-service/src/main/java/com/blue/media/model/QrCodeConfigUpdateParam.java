package com.blue.media.model;

import com.blue.basic.model.exps.BlueException;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update a exist qr config
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class QrCodeConfigUpdateParam extends QrCodeConfigInsertParam {

    private static final long serialVersionUID = 4092725047795505179L;

    private Long id;

    public QrCodeConfigUpdateParam() {
    }

    public QrCodeConfigUpdateParam(Long id, String name, String description, Integer type, String domain, String pathToBeFilled, Integer placeholderCount, List<Long> allowedRoles) {
        super(name, description, type, domain, pathToBeFilled, placeholderCount, allowedRoles);
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
        return "QrCodeConfigUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", allowedRoles=" + allowedRoles +
                '}';
    }

}
