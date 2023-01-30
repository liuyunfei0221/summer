package com.blue.risk.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertHttpMethod;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * illegal mark param
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class IllegalMarkParam implements Serializable, Asserter {

    private static final long serialVersionUID = 3871765044334934199L;

    /**
     * member id
     */
    private Long memberId;

    /**
     * target ip
     */
    private String ip;

    /**
     * request method
     */
    private String method;

    /**
     * request uri
     */
    private String uri;

    /**
     * actions for intercept
     */
    private Boolean mark;

    /**
     * expire seconds
     */
    private Long illegalExpiresSecond;

    public IllegalMarkParam() {
    }

    public IllegalMarkParam(Long memberId, String ip, String method, String uri, Boolean mark, Long illegalExpiresSecond) {
        this.memberId = memberId;
        this.ip = ip;
        this.method = method;
        this.uri = uri;
        this.mark = mark;
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.memberId) && isBlank(this.ip))
            throw new BlueException(INVALID_PARAM);

        assertHttpMethod(this.method, false);

        if (isBlank(uri))
            throw new BlueException(INVALID_PARAM);

        if (isNull(this.mark))
            throw new BlueException(INVALID_PARAM);

        if (isLessThanOrEqualsZero(this.illegalExpiresSecond))
            throw new BlueException(INVALID_PARAM);
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public Long getIllegalExpiresSecond() {
        return illegalExpiresSecond;
    }

    public void setIllegalExpiresSecond(Long illegalExpiresSecond) {
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    @Override
    public String toString() {
        return "IllegalMarkParam{" +
                "memberId=" + memberId +
                ", ip='" + ip + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", mark=" + mark +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                '}';
    }

}