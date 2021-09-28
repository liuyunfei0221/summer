package com.blue.base.component.exception.handler.model;

import com.blue.base.model.base.BlueResponse;

/**
 * http status code and result
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class ExceptionHandleInfo {

    private final Integer code;

    private final BlueResponse<Void> blueResponse;

    public ExceptionHandleInfo(Integer code, BlueResponse<Void> blueResponse) {
        if (code == null)
            throw new RuntimeException("code can't be null");
        if (blueResponse == null)
            throw new RuntimeException("blueVo can't be null");

        this.code = code;
        this.blueResponse = blueResponse;
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
