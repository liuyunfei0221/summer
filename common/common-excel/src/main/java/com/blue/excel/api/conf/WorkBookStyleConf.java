package com.blue.excel.api.conf;


/**
 * excel样式配置
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface WorkBookStyleConf {

    /**
     * 单元格宽度
     *
     * @return
     */
    Integer getColumnWidth();

    /**
     * 单元格高度
     *
     * @return
     */
    Short getColumnHeight();

    /**
     * 字体大小
     *
     * @return
     */
    Short getFontSize();

    /**
     * 字体名称
     *
     * @return
     */
    String getFontName();

}
