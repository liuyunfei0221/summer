package com.blue.media.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertQrCodeGenType;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new qr config
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class QrCodeConfigInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1231929861874694375L;

    protected String name;

    protected String description;

    /**
     * unique qr code type
     */
    protected Integer type;

    /**
     * @see com.blue.basic.constant.media.QrCodeGenType
     */
    protected Integer genHandlerType;

    protected String domain;

    protected String pathToBeFilled;

    protected Integer placeholderCount;

    protected List<Long> allowedRoles;

    public QrCodeConfigInsertParam() {
    }

    public QrCodeConfigInsertParam(String name, String description, Integer type, String domain, String pathToBeFilled, Integer placeholderCount, List<Long> allowedRoles) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.allowedRoles = allowedRoles;
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid name");
        if (isBlank(this.description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid description");
        assertQrCodeGenType(this.genHandlerType, false);
        if (isBlank(this.domain))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid domain");
        if (isBlank(this.pathToBeFilled))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid pathToBeFilled");
        if (isNull(this.placeholderCount) || placeholderCount < 0)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid attributes");
        if (isEmpty(this.allowedRoles))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid allowedRoles");
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

    public Integer getGenHandlerType() {
        return genHandlerType;
    }

    public void setGenHandlerType(Integer genHandlerType) {
        this.genHandlerType = genHandlerType;
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

    public List<Long> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<Long> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public String toString() {
        return "QrCodeConfigInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", genHandlerType=" + genHandlerType +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", allowedRoles=" + allowedRoles +
                '}';
    }
    
}
