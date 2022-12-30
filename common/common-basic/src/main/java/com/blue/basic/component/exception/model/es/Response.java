package com.blue.basic.component.exception.model.es;

import java.io.Serializable;

/**
 * es resp model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Response implements Serializable {

    private static final long serialVersionUID = -5543231004415948539L;

    private Integer status;
    private EsError error;

    public Response() {
    }

    public Response(Integer status, EsError error) {
        this.status = status;
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EsError getError() {
        return error;
    }

    public void setError(EsError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", error=" + error +
                '}';
    }

}
