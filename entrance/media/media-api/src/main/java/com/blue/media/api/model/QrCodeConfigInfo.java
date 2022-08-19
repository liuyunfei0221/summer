package com.blue.media.api.model;


import com.blue.basic.serializer.IdentitiesDeserializer;
import com.blue.basic.serializer.IdentitiesSerializer;
import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * qr code config info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class QrCodeConfigInfo implements Serializable {

    private static final long serialVersionUID = 4786656684754973382L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    private String name;

    private String description;

    /**
     * unique qr code type
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.media.QrCodeGenType
     */
    private Integer genHandlerType;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    /**
     * allowed role ids
     */
    @JsonSerialize(using = IdentitiesSerializer.class)
    @JsonDeserialize(using = IdentitiesDeserializer.class)
    private List<Long> allowedRoles;

    private Integer status;

    public QrCodeConfigInfo() {
    }

    public QrCodeConfigInfo(Long id, String name, String description, Integer type, Integer genHandlerType, String domain,
                            String pathToBeFilled, Integer placeholderCount, List<Long> allowedRoles, Integer status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.genHandlerType = genHandlerType;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.allowedRoles = allowedRoles;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QrCodeConfigInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", genHandlerType=" + genHandlerType +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", allowedRoles=" + allowedRoles +
                ", status=" + status +
                '}';
    }

}