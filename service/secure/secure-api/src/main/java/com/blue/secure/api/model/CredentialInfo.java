package com.blue.secure.api.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * member credential info without member id
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class CredentialInfo implements Serializable {

    private static final long serialVersionUID = -1783008101958304961L;

    private final String credential;

    private final String type;

    private final String access;

    private final String extra;

    public CredentialInfo(String credential, String type, String access, String extra) {
        if (isBlank(type))
            throw new BlueException(BAD_REQUEST);

        this.credential = credential;
        this.type = type;
        this.access = access;
        this.extra = extra;
    }

    public String getCredential() {
        return credential;
    }

    public String getType() {
        return type;
    }

    public String getAccess() {
        return access;
    }

    public String getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "CredentialInfo{" +
                "credential='" + credential + '\'' +
                ", type='" + type + '\'' +
                ", access='" + access + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}



