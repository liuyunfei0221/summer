package com.blue.excel.api.conf;

import static com.blue.excel.constant.WorkBookConf.*;

/**
 * work book style params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class WorkBookStyle implements WorkBookStyleConf {

    /**
     * cell width
     */
    protected Integer columnWidth;

    /**
     * cell height
     */
    protected Short columnHeight;

    /**
     * font size
     */
    protected Short fontSize;

    /**
     * font name
     */
    protected String fontName;


    /**
     * default params
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
