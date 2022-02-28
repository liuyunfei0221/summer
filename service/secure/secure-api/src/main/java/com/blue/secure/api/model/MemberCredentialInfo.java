package com.blue.secure.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * member credential infoS with member id
 *
 * @author DarkBlue
 */
public final class MemberCredentialInfo implements Serializable {

    private static final long serialVersionUID = 8079056879120261243L;

    private final Long memberId;

    private final List<CredentialInfo> credentials;

    public MemberCredentialInfo(Long memberId, List<CredentialInfo> credentials) {
        this.memberId = memberId;
        this.credentials = credentials;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<CredentialInfo> getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return "MemberCredentialInfo{" +
                "memberId=" + memberId +
                ", credentials=" + credentials +
                '}';
    }

}
