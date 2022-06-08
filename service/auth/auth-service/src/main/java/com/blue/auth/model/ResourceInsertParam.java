package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.CommonFunctions.REST_URI_ASSERTER;
import static com.blue.base.common.base.ConstantProcessor.assertHttpMethod;
import static com.blue.base.common.base.ConstantProcessor.assertResourceType;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new resource
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class ResourceInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -5770146088678172629L;

    /**
     * request method str/upper
     */
    private String requestMethod;

    /**
     * module/service name
     */
    private String module;

    /**
     * resource uri
     */
    private String uri;

    /**
     * certificate resource?
     */
    private Boolean authenticate;

    /**
     * decrypt request param?
     */
    private Boolean requestUnDecryption;

    /**
     * encrypt response result?
     */
    private Boolean responseUnEncryption;

    /**
     * has request body?
     */
    private Boolean existenceRequestBody;

    /**
     * has response body?
     */
    private Boolean existenceResponseBody;

    /**
     * @see com.blue.base.constant.auth.ResourceType
     */
    private Integer type;

    /**
     * resource name
     */
    private String name;

    /**
     * desc
     */
    private String description;

    public ResourceInsertParam() {
    }

    public ResourceInsertParam(String requestMethod, String module, String uri, Boolean authenticate,
                               Boolean requestUnDecryption, Boolean responseUnEncryption,
                               Boolean existenceRequestBody, Boolean existenceResponseBody,
                               Integer type, String name, String description) {
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
        this.description = description;
    }

    @Override
    public void asserts() {
        assertHttpMethod(this.requestMethod, false);
        if (isBlank(this.module))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "module can't be blank");
        REST_URI_ASSERTER.accept(uri);
        if (isNull(this.authenticate))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "authenticate can't be null");
        if (isNull(this.requestUnDecryption))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "requestUnDecryption can't be null");
        if (isNull(this.responseUnEncryption))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "responseUnEncryption can't be null");
        if (isNull(this.existenceRequestBody))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceRequestBody can't be null");
        if (isNull(this.existenceResponseBody))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceResponseBody can't be null");
        assertResourceType(this.type, false);
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ResourceInsertParam{" +
                "requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", uri='" + uri + '\'' +
                ", authenticate=" + authenticate +
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
