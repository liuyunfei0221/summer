package com.blue.basic.constant.common;

/**
 * Special prefix
 *
 * @author liuyunfei
 */
public enum BluePrefix {

    /**
     * class path
     */
    CLASS_PATH_PREFIX("classpath"),

    /**
     * attachment name prefix for content disposition
     */
    CONTENT_DISPOSITION_FILE_NAME_PREFIX("attachment; filename=");

    /**
     * value
     */
    public final String prefix;

    BluePrefix(String prefix) {
        this.prefix = prefix;
    }

}
