package com.blue.risk.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.risk.constant.RiskHitRecordSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * risk hit record condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskHitRecordCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 3013093799949929772L;

    private String dataEventType;

    private String dataEventOpType;

    private Long stampBegin;

    private Long stampEnd;

    private String method;

    private String uri;

    private String realUri;

    private Integer responseStatus;

    private Long memberId;

    private Long roleId;

    private String credentialType;

    private String deviceType;

    private Long loginTimeBegin;

    private Long loginTimeEnd;

    private String clientIp;

    private String userAgent;

    private Integer durationSecondsMin;

    private Integer durationSecondsMax;

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
     *
     * @see com.blue.basic.constant.common.BlueBoolean
     */
    private Integer authenticate;

    /**
     * @see com.blue.basic.constant.auth.ResourceType
     */
    private Integer type;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer hitType;

    public RiskHitRecordCondition() {
        super(RiskHitRecordSortAttribute.STAMP.attribute, DESC.identity);
    }

    public RiskHitRecordCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RiskHitRecordCondition(String dataEventType, String dataEventOpType, Long stampBegin, Long stampEnd, String method, String uri, String realUri,
                                  Integer responseStatus, Long memberId, Long roleId, String credentialType, String deviceType, Long loginTimeBegin, Long loginTimeEnd,
                                  String clientIp, String userAgent, Integer durationSecondsMin, Integer durationSecondsMax, Long resourceId, String module, String relativeUri, String absoluteUri, String relationView,
                                  Integer authenticate, Integer type, Integer hitType, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.dataEventType = dataEventType;
        this.dataEventOpType = dataEventOpType;
        this.stampBegin = stampBegin;
        this.stampEnd = stampEnd;
        this.method = method;
        this.uri = uri;
        this.realUri = realUri;
        this.responseStatus = responseStatus;
        this.memberId = memberId;
        this.roleId = roleId;
        this.credentialType = credentialType;
        this.deviceType = deviceType;
        this.loginTimeBegin = loginTimeBegin;
        this.loginTimeEnd = loginTimeEnd;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.durationSecondsMin = durationSecondsMin;
        this.durationSecondsMax = durationSecondsMax;
        this.resourceId = resourceId;
        this.module = module;
        this.relativeUri = relativeUri;
        this.absoluteUri = absoluteUri;
        this.relationView = relationView;
        this.authenticate = authenticate;
        this.type = type;
        this.hitType = hitType;
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

    public Long getStampBegin() {
        return stampBegin;
    }

    public void setStampBegin(Long stampBegin) {
        this.stampBegin = stampBegin;
    }

    public Long getStampEnd() {
        return stampEnd;
    }

    public void setStampEnd(Long stampEnd) {
        this.stampEnd = stampEnd;
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

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public Long getLoginTimeBegin() {
        return loginTimeBegin;
    }

    public void setLoginTimeBegin(Long loginTimeBegin) {
        this.loginTimeBegin = loginTimeBegin;
    }

    public Long getLoginTimeEnd() {
        return loginTimeEnd;
    }

    public void setLoginTimeEnd(Long loginTimeEnd) {
        this.loginTimeEnd = loginTimeEnd;
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

    public Integer getDurationSecondsMin() {
        return durationSecondsMin;
    }

    public void setDurationSecondsMin(Integer durationSecondsMin) {
        this.durationSecondsMin = durationSecondsMin;
    }

    public Integer getDurationSecondsMax() {
        return durationSecondsMax;
    }

    public void setDurationSecondsMax(Integer durationSecondsMax) {
        this.durationSecondsMax = durationSecondsMax;
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

    public Integer getHitType() {
        return hitType;
    }

    public void setHitType(Integer hitType) {
        this.hitType = hitType;
    }

    @Override
    public String toString() {
        return "RiskHitRecordCondition{" +
                "dataEventType='" + dataEventType + '\'' +
                ", dataEventOpType='" + dataEventOpType + '\'' +
                ", stampBegin=" + stampBegin +
                ", stampEnd=" + stampEnd +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", realUri='" + realUri + '\'' +
                ", responseStatus=" + responseStatus +
                ", memberId=" + memberId +
                ", roleId=" + roleId +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTimeBegin=" + loginTimeBegin +
                ", loginTimeEnd=" + loginTimeEnd +
                ", clientIp='" + clientIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", durationSecondsMin=" + durationSecondsMin +
                ", durationSecondsMax=" + durationSecondsMax +
                ", resourceId=" + resourceId +
                ", module='" + module + '\'' +
                ", relativeUri='" + relativeUri + '\'' +
                ", absoluteUri='" + absoluteUri + '\'' +
                ", relationView='" + relationView + '\'' +
                ", authenticate=" + authenticate +
                ", type=" + type +
                ", hitType=" + hitType +
                '}';
    }
    
}
