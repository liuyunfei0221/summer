package com.blue.secure.model;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;

/**
 * params for update a resource
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ResourceUpdateParam extends ResourceInsertParam {

    private static final long serialVersionUID = 5746658572192224965L;

    private Long id;

    public ResourceUpdateParam() {
    }

    public ResourceUpdateParam(String requestMethod, String module, String uri, Boolean authenticate, Boolean requestUnDecryption, Boolean responseUnEncryption, Boolean existenceRequestBody, Boolean existenceResponseBody, Integer type, String name, String description, Long id) {
        super(requestMethod, module, uri, authenticate, requestUnDecryption, responseUnEncryption, existenceRequestBody, existenceResponseBody, type, name, description);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message, null);

        this.id = id;
    }

    public Long getId() {
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message, null);

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResourceUpdateParam{" +
                "id='" + id + '\'' +
                ", requestMethod='" + super.getRequestMethod() + '\'' +
                ", module='" + super.getModule() + '\'' +
                ", uri='" + super.getUri() + '\'' +
                ", authenticate=" + super.getAuthenticate() +
                ", requestUnDecryption=" + super.getRequestUnDecryption() +
                ", responseUnEncryption=" + super.getResponseUnEncryption() +
                ", existenceRequestBody=" + super.getExistenceRequestBody() +
                ", existenceResponseBody=" + super.getExistenceResponseBody() +
                ", type=" + super.getType() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                '}';
    }

}
