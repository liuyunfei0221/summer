package com.blue.excel.api.conf;

import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.function.BiConsumer;

/**
 * excel元素封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface WorkBookElementConf<T> {

    /**
     * 表头
     *
     * @return
     */
    String[] getHeaders();

    /**
     * 元素与EXCEL行封装器
     *
     * @return
     */
    BiConsumer<XSSFRow, T> getRowElementPackager();

}
