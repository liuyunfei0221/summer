package com.blue.secure.repository.entity;

/**
 * 资源
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class Resource {

    /**
     * 主键
     */
    private Long id;

    /**
     * 请求方式/大写
     */
    private String requestMethod;

    /**
     * 服务
     */
    private String module;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 认证标识 1需要认证 0免认证
     */
    private Boolean authenticate;

    /**
     * 请求数据不解密 1不解密 0解密
     */
    private Boolean preUnDecryption;

    /**
     * 响应数据不加密 1不加密 0加密
     */
    private Boolean postUnEncryption;

    /**
     * 是否有请求体 1有 0没有
     */
    private Boolean existenceRequestBody;

    /**
     * 是否有响应体 1有 0没有
     */
    private Boolean existenceResponseBody;

    /**
     * 资源类型 1前台api 2后台api 3对外提供api
     */
    private Integer type;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long updater;

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
        this.requestMethod = requestMethod == null ? null : requestMethod.trim();
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
        this.uri = uri == null ? null : uri.trim();
    }

    public Boolean getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Boolean authenticate) {
        this.authenticate = authenticate;
    }

    public Boolean getPreUnDecryption() {
        return preUnDecryption;
    }

    public void setPreUnDecryption(Boolean preUnDecryption) {
        this.preUnDecryption = preUnDecryption;
    }

    public Boolean getPostUnEncryption() {
        return postUnEncryption;
    }

    public void setPostUnEncryption(Boolean postUnEncryption) {
        this.postUnEncryption = postUnEncryption;
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
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
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
        return "Resource{" +
                "id=" + id +
                ", requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", uri='" + uri + '\'' +
                ", authenticate=" + authenticate +
                ", preUnDecryption=" + preUnDecryption +
                ", postUnEncryption=" + postUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}