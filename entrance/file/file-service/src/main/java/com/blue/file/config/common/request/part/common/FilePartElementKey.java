package com.blue.file.config.common.request.part.common;

/**
 * 文件属性key
 *
 * @author liuyunfei
 * @date 2021/9/1
 * @apiNote
 */
public enum FilePartElementKey {

    /**
     * 封装文件数据的具体类
     */
    PART_CLASS("class", "封装文件数据的具体类"),

    /**
     * 文件数据名称
     */
    PART_NAME("name", "文件数据名称"),

    /**
     * 文件头
     */
    PART_HEADERS("headers", "文件头"),

    /**
     * 文件名
     */
    FILE_NAME("fileName", "文件名");


    public String identity;

    public String disc;

    FilePartElementKey(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
