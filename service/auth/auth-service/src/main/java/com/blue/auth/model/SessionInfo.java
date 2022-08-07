package com.blue.auth.model;

import com.blue.member.api.model.MemberBasicInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * session info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class SessionInfo implements Serializable {

    private static final long serialVersionUID = 4914045452462954741L;

    /**
     * member
     */
    private final MemberBasicInfo member;

    /**
     * Extra
     */
    private final Map<String, Object> extra;

    public SessionInfo(MemberBasicInfo member, Map<String, Object> extra) {
        this.member = member;
        this.extra = extra;
    }

    public MemberBasicInfo getMember() {
        return member;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "SessionInfo{" +
                "member=" + member +
                ", extra=" + extra +
                '}';
    }

}
