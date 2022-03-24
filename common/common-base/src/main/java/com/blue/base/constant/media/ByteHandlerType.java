package com.blue.base.constant.media;

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
    LOCAL_DISK("ld");

    public final String identity;

    ByteHandlerType(String identity) {
        this.identity = identity;
    }

}
