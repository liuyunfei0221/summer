package com.blue.base.component.exception.handler.model;

import com.blue.base.model.base.BlueResult;

/**
 * 状态码及vo封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class ExceptionHandleInfo {

    private final Integer code;

    private final BlueResult<Void> blueResult;

    public ExceptionHandleInfo(Integer code, BlueResult<Void> blueResult) {
        if (code == null)
            throw new RuntimeException("code不能为空");
        if (blueResult == null)
            throw new RuntimeException("blueVo不能为空");

        this.code = code;
        this.blueResult = blueResult;
    }

    public Integer getCode() {
        return code;
    }

    public BlueResult<Void> getBlueVo() {
        return blueResult;
    }

    @Override
    public String toString() {
        return "ExceptionHandleInfo{" +
                "code=" + code +
                ", blueVo=" + blueResult +
                '}';
    }
}
