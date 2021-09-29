package com.blue.file.common.request.part.common;

/**
 * file part element attr
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
     * file name
     */
    FILE_NAME("fileName", "file name");


    public String identity;

    public String disc;

    FilePartElementKey(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
