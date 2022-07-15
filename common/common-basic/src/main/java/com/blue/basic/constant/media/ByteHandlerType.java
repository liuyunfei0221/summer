package com.blue.basic.constant.media;

/**
 * byte handler type
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment"})
public enum ByteHandlerType {

    /**
     * local disk
     */
    LOCAL_DISK("ld"),

    /**
     * tencent cos
     */
    COS("cos");

    public final String identity;

    ByteHandlerType(String identity) {
        this.identity = identity;
    }

}
