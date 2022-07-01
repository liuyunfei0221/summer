package com.blue.media.api.model;


import java.io.Serializable;

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

    private Integer status;

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
                ", type=" + type +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", fileType='" + fileType + '\'' +
                ", status=" + status +
                '}';
    }

}