package com.blue.base.component.exception.handler.model;

import com.blue.base.model.base.BlueResponse;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * http status code and result
 *
 * @author DarkBlue
 */
public final class ExceptionHandleInfo {

    private final Integer status;

    private final BlueResponse<Void> blueResponse;

    public ExceptionHandleInfo(Integer status, BlueResponse<Void> blueResponse) {
        this.status = status != null ? status : INTERNAL_SERVER_ERROR.code;
        this.blueResponse = blueResponse != null ? blueResponse : new BlueResponse<>(INTERNAL_SERVER_ERROR.code, null, INTERNAL_SERVER_ERROR.message);
    }

    public Integer getStatus() {
        return status;
    }

    public BlueResponse<Void> getBlueVo() {
        return blueResponse;
    }

    @Override
    public String toString() {
        return "ExceptionHandleInfo{" +
                "status=" + status +
                ", blueVo=" + blueResponse +
                '}';
    }
}
