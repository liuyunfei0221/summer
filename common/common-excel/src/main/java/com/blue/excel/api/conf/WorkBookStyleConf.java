package com.blue.excel.api.conf;


/**
 * work book style conf
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface WorkBookStyleConf {

    /**
     * cell width
     *
     * @return
     */
    Integer getColumnWidth();

    /**
     * cell height
     *
     * @return
     */
    Short getColumnHeight();

    /**
     * font size
     *
     * @return
     */
    Short getFontSize();

    /**
     * font name
     *
     * @return
     */
    String getFontName();

}
