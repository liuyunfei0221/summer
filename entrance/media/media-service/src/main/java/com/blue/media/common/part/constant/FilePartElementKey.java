package com.blue.media.common.part.constant;

/**
 * media part element attr
 *
 * @author liuyunfei
 * @date 2021/9/1
 * @apiNote
 */
public enum FilePartElementKey {

    /**
     * target handler class
     */
    PART_CLASS("class", "target handler class"),

    /**
     * part name for handle
     */
    PART_NAME("name", "part name for handle"),

    /**
     * part headers
     */
    PART_HEADERS("headers", "part headers"),

    /**
     * media name
     */
    FILE_NAME("fileName", "media name");


    public final String identity;

    public final String disc;

    FilePartElementKey(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
