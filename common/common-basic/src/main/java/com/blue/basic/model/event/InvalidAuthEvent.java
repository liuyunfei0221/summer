package com.blue.basic.model.event;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * redis key expire info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class InvalidAuthEvent implements Serializable {

    private static final long serialVersionUID = 713277204676056312L;

    /**
     * member id
     */
    private Long memberId;

    public InvalidAuthEvent() {
    }

    public InvalidAuthEvent(Long memberId) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "InvalidAccessEvent{" +
                "memberId=" + memberId +
                '}';
    }

}
