package com.blue.media.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new qr config
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class QrCodeConfigInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1231929861874694375L;

    private String name;

    /**
     * qr code type
     *
     * @see com.blue.base.constant.media.QrCodeType
     */
    private Integer type;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    private String fileType;

    public QrCodeConfigInsertParam() {
    }

    public QrCodeConfigInsertParam(String name, Integer type, String domain, String pathToBeFilled, Integer placeholderCount, String fileType) {
        this.name = name;
        this.type = type;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.fileType = fileType;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid name");
        assertBulletinType(this.type, false);
        if (isBlank(this.domain))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid domain");
        if (isBlank(this.pathToBeFilled))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid pathToBeFilled");
        if (isNull(this.placeholderCount) || placeholderCount < 0)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid attributes");
        if (isBlank(this.fileType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid fileType");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPathToBeFilled() {
        return pathToBeFilled;
    }

    public void setPathToBeFilled(String pathToBeFilled) {
        this.pathToBeFilled = pathToBeFilled;
    }

    public Integer getPlaceholderCount() {
        return placeholderCount;
    }

    public void setPlaceholderCount(Integer placeholderCount) {
        this.placeholderCount = placeholderCount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "QrCodeConfigInsertParam{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", fileType='" + fileType + '\'' +
                '}';
    }

}
