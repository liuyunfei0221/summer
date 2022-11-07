package com.blue.risk.repository.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * risk hit record
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskHitRecord implements Serializable {

    private static final long serialVersionUID = -5000937958564581762L;

    private Long id;

    private String dataEventType;

    private String dataEventOpType;

    private Long stamp;

    private String createDate;

    private String method;

    private String uri;

    private String realUri;

    private String requestBody;

    private String requestExtra;

    private Integer responseStatus;

    private String responseBody;

    private String responseExtra;

    private String requestId;

    private String metadata;

    private String jwt;

    private Long memberId;

    private Long[] roleIds;

    private String credentialType;

    private String deviceType;

    private Long loginTime;

    private String clientIp;

    private String userAgent;

    private String secKey;

    private Integer requestUnDecryption;

    private Integer responseUnEncryption;

    private Integer existenceRequestBody;

    private Integer existenceResponseBody;

    private Integer durationSeconds;

    /**
     * resource for intercept
     */
    private String resourceKey;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer hitType;

    /**
     * expire seconds
     */
    private Long illegalExpiresSecond;

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

    public String getDataEventOpType() {
        return dataEventOpType;
    }

    public void setDataEventOpType(String dataEventOpType) {
        this.dataEventOpType = dataEventOpType;
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

    public String getRequestExtra() {
        return requestExtra;
    }

    public void setRequestExtra(String requestExtra) {
        this.requestExtra = requestExtra;
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

    public String getResponseExtra() {
        return responseExtra;
    }

    public void setResponseExtra(String responseExtra) {
        this.responseExtra = responseExtra;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Integer getHitType() {
        return hitType;
    }

    public void setHitType(Integer hitType) {
        this.hitType = hitType;
    }

    public Long getIllegalExpiresSecond() {
        return illegalExpiresSecond;
    }

    public void setIllegalExpiresSecond(Long illegalExpiresSecond) {
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    @Override
    public String toString() {
        return "RiskHitRecord{" +
                "id=" + id +
                ", dataEventType='" + dataEventType + '\'' +
                ", dataEventOpType='" + dataEventOpType + '\'' +
                ", stamp=" + stamp +
                ", createDate='" + createDate + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", realUri='" + realUri + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", requestExtra='" + requestExtra + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseBody='" + responseBody + '\'' +
                ", responseExtra='" + responseExtra + '\'' +
                ", requestId='" + requestId + '\'' +
                ", metadata='" + metadata + '\'' +
                ", jwt='" + jwt + '\'' +
                ", memberId=" + memberId +
                ", roleIds=" + Arrays.toString(roleIds) +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime=" + loginTime +
                ", clientIp='" + clientIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", secKey='" + secKey + '\'' +
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", durationSeconds=" + durationSeconds +
                ", resourceKey='" + resourceKey + '\'' +
                ", hitType=" + hitType +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                '}';
    }

}
