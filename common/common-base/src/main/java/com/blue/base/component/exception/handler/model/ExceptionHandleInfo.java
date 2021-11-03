package com.blue.base.component.exception.handler.model;

import com.blue.base.model.base.BlueResponse;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * http status code and result
 *
 * @author DarkBlue
 */
public final class ExceptionHandleInfo {

    private final Integer code;

    private final BlueResponse<Void> blueResponse;

    public ExceptionHandleInfo(Integer code, BlueResponse<Void> blueResponse) {
        this.code = code != null ? code : INTERNAL_SERVER_ERROR.code;
        this.blueResponse = blueResponse != null ? blueResponse : new BlueResponse<>(INTERNAL_SERVER_ERROR.code, null, INTERNAL_SERVER_ERROR.message);
    }

    public Integer getCode() {
        return code;
    }

    public BlueResponse<Void> getBlueVo() {
        return blueResponse;
    }

    @Override
    public String toString() {
        return "ExceptionHandleInfo{" +
                "code=" + code +
                ", blueVo=" + blueResponse +
                '}';
    }
}
