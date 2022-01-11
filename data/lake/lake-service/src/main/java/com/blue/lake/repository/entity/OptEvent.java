package com.blue.lake.repository.entity;

import java.io.Serializable;

/**
 * option event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class OptEvent implements Serializable {

    private static final long serialVersionUID = 2594284948929033405L;


    private Long id;

    private String dataEventType;

    private Long stamp;

    private String createDate;

    private String method;

    private String uri;

    private String realUri;

    private String requestBody;

    private Integer responseStatus;

    private String responseBody;

    private String requestId;

    private String metadata;

    private String jwt;

    private String access;

    private String clientIp;

    private String secKey;

    private Integer requestUnDecryption;

    private Integer responseUnEncryption;

    private Integer existenceRequestBody;

    private Integer existenceResponseBody;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataEventType() {
        return dataEventType;
    }

    public void setDataEventType(String dataEventType) {
        this.dataEventType = dataEventType;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRealUri() {
        return realUri;
    }

    public void setRealUri(String realUri) {
        this.realUri = realUri;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public Integer getRequestUnDecryption() {
        return requestUnDecryption;
    }

    public void setRequestUnDecryption(Integer requestUnDecryption) {
        this.requestUnDecryption = requestUnDecryption;
    }

    public Integer getResponseUnEncryption() {
        return responseUnEncryption;
    }

    public void setResponseUnEncryption(Integer responseUnEncryption) {
        this.responseUnEncryption = responseUnEncryption;
    }

    public Integer getExistenceRequestBody() {
        return existenceRequestBody;
    }

    public void setExistenceRequestBody(Integer existenceRequestBody) {
        this.existenceRequestBody = existenceRequestBody;
    }

    public Integer getExistenceResponseBody() {
        return existenceResponseBody;
    }

    public void setExistenceResponseBody(Integer existenceResponseBody) {
        this.existenceResponseBody = existenceResponseBody;
    }

    @Override
    public String toString() {
        return "OptEvent{" +
                "id=" + id +
                ", dataEventType='" + dataEventType + '\'' +
                ", stamp=" + stamp +
                ", createDate='" + createDate + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", realUri='" + realUri + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseBody='" + responseBody + '\'' +
                ", requestId='" + requestId + '\'' +
                ", metadata='" + metadata + '\'' +
                ", jwt='" + jwt + '\'' +
                ", access='" + access + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", secKey='" + secKey + '\'' +
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                '}';
    }

}