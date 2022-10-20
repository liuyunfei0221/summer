package com.blue.media.api.model;


import com.blue.basic.constant.media.QrCodeType;

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

    private Long id;

    private String name;

    private String description;

    /**
     * @see QrCodeType
     */
    private Integer type;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    /**
     * allowed role ids
     */
    private List<Long> allowedRoles;

    public QrCodeConfigInfo() {
    }

    public QrCodeConfigInfo(Long id, String name, String description, Integer type, String domain,
                            String pathToBeFilled, Integer placeholderCount, List<Long> allowedRoles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.allowedRoles = allowedRoles;
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
        return "QrCodeConfigInfo{" +
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