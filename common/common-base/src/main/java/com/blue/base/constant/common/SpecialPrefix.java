package com.blue.base.constant.common;

/**
 * Special prefix
 *
 * @author liuyunfei
 */
public enum SpecialPrefix {

    /**
     * attachment name prefix for content disposition
     */
    CONTENT_DISPOSITION_FILE_NAME_PREFIX("attachment; filename=");

    /**
     * value
     */
    public final String prefix;

    SpecialPrefix(String prefix) {
        this.prefix = prefix;
    }

}
