package com.blue.auth.model;

import com.blue.auth.constant.ResourceSortAttribute;
import com.blue.basic.model.common.SortCondition;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

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

    private String uriLike;

    private String relationViewLike;

    private Boolean authenticate;

    private Boolean requestUnDecryption;

    private Boolean responseUnEncryption;

    private Boolean existenceRequestBody;

    private Boolean existenceResponseBody;

    private Integer type;

    private String nameLike;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public ResourceCondition() {
        super(ResourceSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public ResourceCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public ResourceCondition(Long id, String requestMethod, String module, String uriLike, String relationViewLike, Boolean authenticate, Boolean requestUnDecryption, Boolean responseUnEncryption, Boolean existenceRequestBody, Boolean existenceResponseBody,
                             Integer type, String nameLike, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.requestMethod = requestMethod;
        this.module = module;
        this.uriLike = uriLike;
        this.relationViewLike = relationViewLike;
        this.authenticate = authenticate;
        this.requestUnDecryption = requestUnDecryption;
        this.responseUnEncryption = responseUnEncryption;
        this.existenceRequestBody = existenceRequestBody;
        this.existenceResponseBody = existenceResponseBody;
        this.type = type;
        this.nameLike = nameLike;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
        this.creator = creator;
        this.updater = updater;
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

    public String getUriLike() {
        return uriLike;
    }

    public void setUriLike(String uriLike) {
        this.uriLike = uriLike;
    }

    public String getRelationViewLike() {
        return relationViewLike;
    }

    public void setRelationViewLike(String relationViewLike) {
        this.relationViewLike = relationViewLike;
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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return "ResourceCondition{" +
                "id=" + id +
                ", requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", uriLike='" + uriLike + '\'' +
                ", relationViewLike='" + relationViewLike + '\'' +
                ", authenticate=" + authenticate +
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type=" + type +
                ", nameLike='" + nameLike + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", creator=" + creator +
                ", updater=" + updater +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}
