package com.blue.risk.repository.entity;

import com.blue.basic.serializer.Long2StringSerializer;
import com.blue.basic.serializer.LongArray2StringArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * risk hit record
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class RiskHitRecord implements Serializable {

    private static final long serialVersionUID = -5000937958564581762L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long cursor;
    private String dataEventType;

    private String dataEventOpType;

    @JsonSerialize(using = Long2StringSerializer.class)
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

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    @JsonSerialize(using = LongArray2StringArraySerializer.class)
    private Long[] roleIds;

    private String credentialType;

    private String deviceType;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long loginTime;

    private String clientIp;

    private String userAgent;

    private String secKey;

    private Integer requestUnDecryption;

    private Integer responseUnEncryption;

    private Integer existenceRequestBody;

    private Integer existenceResponseBody;

    private Integer durationSeconds;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long resourceId;

    /**
     * module/service name
     */
    private String module;

    /**
     * relative uri
     */
    private String relativeUri;

    /**
     * absolute uri
     */
    private String absoluteUri;

    /**
     * relation view
     */
    private String relationView;

    /**
     * authenticate 1.yes 0.no
     */
    private Integer authenticate;

    /**
     * @see com.blue.basic.constant.auth.ResourceType
     */
    private Integer type;

    /**
     * resource name
     */
    private String name;

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

    public Long getCursor() {
        return cursor;
    }

    public void setCursor(Long cursor) {
        this.cursor = cursor;
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

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRelativeUri() {
        return relativeUri;
    }

    public void setRelativeUri(String relativeUri) {
        this.relativeUri = relativeUri;
    }

    public String getAbsoluteUri() {
        return absoluteUri;
    }

    public void setAbsoluteUri(String absoluteUri) {
        this.absoluteUri = absoluteUri;
    }

    public String getRelationView() {
        return relationView;
    }

    public void setRelationView(String relationView) {
        this.relationView = relationView;
    }

    public Integer getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Integer authenticate) {
        this.authenticate = authenticate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", cursor=" + cursor +
                ", dataEventType='" + dataEventType + '\'' +
                ", dataEventOpType='" + dataEventOpType + '\'' +
                ", stamp=" + stamp +
                ", createDate='" + createDate + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", realUri='" + realUri + '\'' +
                '}';
    }

}
