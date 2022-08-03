package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.*;

/**
 * params for update a resource
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ResourceUpdateParam extends ResourceInsertParam {

    private static final long serialVersionUID = 5746658572192224965L;

    private Long id;

    public ResourceUpdateParam() {
    }

    public ResourceUpdateParam(Long id, String requestMethod, String module, String uri, String relationView, Boolean authenticate, Boolean requestUnDecryption, Boolean responseUnEncryption, Boolean existenceRequestBody, Boolean existenceResponseBody, Integer type, String name, String description) {
        super(requestMethod, module, uri, relationView, authenticate, requestUnDecryption, responseUnEncryption, existenceRequestBody, existenceResponseBody, type, name, description);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        super.asserts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResourceUpdateParam{" +
                "id=" + id +
                ", requestMethod='" + requestMethod + '\'' +
                ", module='" + module + '\'' +
                ", uri='" + uri + '\'' +
                ", relationView='" + relationView + '\'' +
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
