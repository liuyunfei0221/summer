package com.blue.media.constant;

/**
 * column names of download history
 *
 * @author liuyunfei
 */
public enum DownloadHistoryColumnName {

    /**
     * create time
     */
    CREATE_TIME("createTime");

    public final String name;

    DownloadHistoryColumnName(String name) {
        this.name = name;
    }

}