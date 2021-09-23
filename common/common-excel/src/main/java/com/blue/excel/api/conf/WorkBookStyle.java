package com.blue.excel.api.conf;

import static com.blue.excel.constant.WorkBookConf.*;

/**
 * excel样式参数
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class WorkBookStyle implements WorkBookStyleConf {

    /**
     * 列宽
     */
    protected Integer columnWidth;

    /**
     * 行高
     */
    protected Short columnHeight;

    /**
     * 字体大小
     */
    protected Short fontSize;

    /**
     * 字体
     */
    protected String fontName;


    /**
     * 默认参数
     */
    public WorkBookStyle() {
        this.columnWidth = DEFAULT_COLUMN_WIDTH;
        this.columnHeight = DEFAULT_COLUMN_HEIGHT;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.fontName = DEFAULT_FONT_NAME;
    }

    public WorkBookStyle(Integer columnWidth, Short columnHeight, Short fontSize, String fontName) {
        this.columnWidth = columnWidth;
        this.columnHeight = columnHeight;
        this.fontSize = fontSize;
        this.fontName = fontName;
    }

    @Override
    public Integer getColumnWidth() {
        return columnWidth;
    }

    @Override
    public Short getColumnHeight() {
        return columnHeight;
    }

    @Override
    public Short getFontSize() {
        return fontSize;
    }

    @Override
    public String getFontName() {
        return fontName;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setColumnHeight(Short columnHeight) {
        this.columnHeight = columnHeight;
    }

    public void setFontSize(Short fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    @Override
    public String toString() {
        return "WorkBookStyle{" +
                "columnWidth=" + columnWidth +
                ", columnHeight=" + columnHeight +
                ", fontSize=" + fontSize +
                ", fontName='" + fontName + '\'' +
                '}';
    }

}
