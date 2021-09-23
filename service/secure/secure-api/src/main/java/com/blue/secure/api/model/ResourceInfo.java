package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * api层资源信息
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ResourceInfo implements Serializable {

    private static final long serialVersionUID = 7791643860296140833L;

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
     * 资源相对路径
     */
    private String relativeUri;

    /**
     * 资源绝对路径
     */
    private String absoluteUri;

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
     * 资源类型 前台api 后台api 对外提供api
     */
    private String type;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    public ResourceInfo() {
    }

    public ResourceInfo(Long id, String requestMethod, String module, String relativeUri, String absoluteUri,
                        Boolean authenticate, Boolean preUnDecryption, Boolean postUnEncryption, Boolean existenceRequestBody,
                        Boolean existenceResponseBody, String type, String name, String description) {
        this.id = id;
        this.requestMethod = requestMethod;
        this.module = module;
        this.relativeUri = relativeUri;
        this.absoluteUri = absoluteUri;
        this.authenticate = authenticate;
        this.preUnDecryption = preUnDecryption;
        this.postUnEncryption = postUnEncryption;
        this.existenceRequestBody = existenceRequestBody;
        this.existenceResponseBody = existenceResponseBody;
        this.type = type;
        this.name = name;
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "ResourceInfo{" +
                "id=" + id +
                ", requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", relativeUri='" + relativeUri + '\'' +
                ", absoluteUri='" + absoluteUri + '\'' +
                ", authenticate=" + authenticate +
                ", preUnDecryption=" + preUnDecryption +
                ", postUnEncryption=" + postUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
