package com.blue.verify.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * verify message
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyMessage implements Serializable {

    private static final long serialVersionUID = 2100414210141423667L;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see com.blue.basic.constant.verify.VerifyBusinessType
     */
    private String businessType;

    /**
     * if verifyType is IMAGE, destination will be ignored.
     * if verifyType is SMS, destination should be a phone num.
     * if verifyType is MAIL, destination should be an email address.
     */
    private String destination;

    private String verify;

    private List<String> languages;

    public VerifyMessage() {
    }

    public VerifyMessage(String verifyType, String businessType, String destination, String verify, List<String> languages) {
        this.verifyType = verifyType;
        this.businessType = businessType;
        this.destination = destination;
        this.verify = verify;
        this.languages = languages;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "VerifyMessage{" +
                "verifyType='" + verifyType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", destination='" + destination + '\'' +
                ", verify='" + verify + '\'' +
                ", languages=" + languages +
                '}';
    }

}
