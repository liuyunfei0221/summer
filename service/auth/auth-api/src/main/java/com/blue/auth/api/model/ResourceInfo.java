package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * resource info for rest
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ResourceInfo implements Serializable {

    private static final long serialVersionUID = 7791643860296140833L;

    private Long id;

    /**
     * request method/upper
     */
    private String requestMethod;

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
     * authenticate 1.yes 0.no
     */
    private Boolean authenticate;

    /**
     * decrypt request params? 1.no 0.yes
     */
    private Boolean requestUnDecryption;

    /**
     * encrypt response result? 1.no 0.yes
     */
    private Boolean responseUnEncryption;

    /**
     * exist request body? 1.yes 0.no
     */
    private Boolean existenceRequestBody;

    /**
     * exist response body? 1.yes 0.no
     */
    private Boolean existenceResponseBody;

    /**
     * resource type: 1.client api 2.manager api 3.open api
     */
    private String type;

    /**
     * resource name
     */
    private String name;

    /**
     * resource disc
     */
    private String description;

    public ResourceInfo() {
    }

    public ResourceInfo(Long id, String requestMethod, String module, String relativeUri, String absoluteUri,
                        Boolean authenticate, Boolean requestUnDecryption, Boolean responseUnEncryption, Boolean existenceRequestBody,
                        Boolean existenceResponseBody, String type, String name, String description) {
        this.id = id;
        this.requestMethod = requestMethod;
        this.module = module;
        this.relativeUri = relativeUri;
        this.absoluteUri = absoluteUri;
        this.authenticate = authenticate;
        this.requestUnDecryption = requestUnDecryption;
        this.responseUnEncryption = responseUnEncryption;
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
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
