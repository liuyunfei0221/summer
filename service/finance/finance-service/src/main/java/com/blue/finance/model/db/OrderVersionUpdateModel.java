package com.blue.finance.model.db;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * order version update model
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class OrderVersionUpdateModel implements Serializable, Asserter {

    private static final long serialVersionUID = -3365516142726157387L;

    private Long id;

    private Integer originalVersion;

    private Integer destVersion;

    private Long updateTime;

    public OrderVersionUpdateModel() {
    }

    public OrderVersionUpdateModel(Long id, Integer originalVersion, Integer destVersion, Long updateTime) {
        this.id = id;
        this.originalVersion = originalVersion;
        this.destVersion = destVersion;
        this.updateTime = updateTime;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);

        if (isNull(this.originalVersion) || this.originalVersion < 0)
            throw new BlueException(INVALID_PARAM);
        if (isNull(this.destVersion) || this.destVersion < 0)
            throw new BlueException(INVALID_PARAM);
        if (this.destVersion <= this.originalVersion)
            throw new BlueException(INVALID_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOriginalVersion() {
        return originalVersion;
    }

    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    public Integer getDestVersion() {
        return destVersion;
    }

    public void setDestVersion(Integer destVersion) {
        this.destVersion = destVersion;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "OrderVersionUpdateModel{" +
                "id=" + id +
                ", originalVersion=" + originalVersion +
                ", destVersion=" + destVersion +
                ", updateTime=" + updateTime +
                '}';
    }

}
