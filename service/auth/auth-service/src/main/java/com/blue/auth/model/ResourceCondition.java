package com.blue.auth.model;

import com.blue.auth.constant.ResourceSortAttribute;
import com.blue.base.constant.base.SortType;
import com.blue.base.model.base.SortCondition;

import java.io.Serializable;

/**
 * resource condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ResourceCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -2623160339413516868L;

    private Long id;

    private String requestMethod;

    private String module;

    private String uri;

    private Boolean authenticate;

    private Boolean requestUnDecryption;

    private Boolean responseUnEncryption;

    private Boolean existenceRequestBody;

    private Boolean existenceResponseBody;

    private Integer type;

    private String name;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public ResourceCondition() {
        super(ResourceSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public ResourceCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public ResourceCondition(String sortAttribute, String sortType, Long id, String requestMethod, String module, String uri, Boolean authenticate, Boolean requestUnDecryption, Boolean responseUnEncryption, Boolean existenceRequestBody, Boolean existenceResponseBody,
                             Integer type, String name, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd) {
        super(sortAttribute, sortType);
        this.id = id;
        this.requestMethod = requestMethod;
        this.module = module;
        this.uri = uri;
        this.authenticate = authenticate;
        this.requestUnDecryption = requestUnDecryption;
        this.responseUnEncryption = responseUnEncryption;
        this.existenceRequestBody = existenceRequestBody;
        this.existenceResponseBody = existenceResponseBody;
        this.type = type;
        this.name = name;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Boolean authenticate) {
        this.authenticate = authenticate;
    }

    public Boolean getRequestUnDecryption() {
        return requestUnDecryption;
    }

    public void setRequestUnDecryption(Boolean requestUnDecryption) {
        this.requestUnDecryption = requestUnDecryption;
    }

    public Boolean getResponseUnEncryption() {
        return responseUnEncryption;
    }

    public void setResponseUnEncryption(Boolean responseUnEncryption) {
        this.responseUnEncryption = responseUnEncryption;
    }

    public Boolean getExistenceRequestBody() {
        return existenceRequestBody;
    }

    public void setExistenceRequestBody(Boolean existenceRequestBody) {
        this.existenceRequestBody = existenceRequestBody;
    }

    public Boolean getExistenceResponseBody() {
        return existenceResponseBody;
    }

    public void setExistenceResponseBody(Boolean existenceResponseBody) {
        this.existenceResponseBody = existenceResponseBody;
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

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Long updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Long getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Long updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    @Override
    public String toString() {
        return "ResourceCondition{" +
                "id=" + id +
                ", requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", uri='" + uri + '\'' +
                ", authenticate=" + authenticate +
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }

}
