package com.blue.base.model.common;

import com.blue.base.inter.Asserter;

import java.io.Serializable;

import static com.blue.base.common.base.ConstantProcessor.assertStatus;

/**
 * status param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class StatusParam implements Serializable, Asserter {

    private static final long serialVersionUID = 3631693809321831634L;

    /**
     * id
     */
    private Integer status;

    public StatusParam() {
    }

    public StatusParam(Integer status) {
        this.status = status;
    }

    @Override
    public void asserts() {
        assertStatus(this.status, false);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusParam{" +
                "status=" + status +
                '}';
    }

}
